package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.helpers.Helper;
import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model.*;
import com.Core_Service.model_request.PrivateMessageRequest;
import com.Core_Service.model_request.ReviewCreateRequest;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.ReviewResponse;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.repository.cache_repository.SeriesCacheRepository;
import com.Core_Service.repository.db_repository.ReviewRepository;
import com.Core_Service.repository.db_repository.SeriesRepository;
import com.Core_Service.repository.db_repository.UserRepository;
import org.commonDTO.SeriesBuyMessage;
import org.commonDTO.SeriesCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableCaching
@CacheConfig(cacheNames = "series_cache", cacheManager = "customCacheManager")
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private SeriesCacheRepository cacheRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public SeriesResponse addSeries(SeriesCreateRequest seriesCreateRequest) {
        Series series = Series.builder().name(seriesCreateRequest.getName())
                .genre(seriesCreateRequest.getGenre().toString())
                .description(seriesCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(seriesCreateRequest.getPrice()).build();
        series = seriesRepository.save(series);

        cacheRepository.clearCacheBySeriesId(series.getId());
        cacheRepository.clearCacheBySeriesGenre(series.getGenre());

        /**                                  -----------------------------
         *  SeriesCreationMessage ======>>> | SeriesCreationMessageTopic |
         *                                  -----------------------------
         */
        streamBridge.send("SeriesCreationMessageTopic", SeriesCreationMessage.builder()
                .id(series.getId())
                .name(series.getName())
                .genre(series.getGenre())
                .description(series.getDescription())
                .searchableDescription(series.getDescription().toLowerCase())
                .posterURL(
                        StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + series.getUniquePosterId()
                )
                .price(series.getPrice())
                .rating(series.getRating() == null ? -1 : series.getRating())
                .createdAt(series.getCreatedAt())
                .build());

        notificationService.notifyAllUsers("New series has been added, pay and watch " + series.getName() + " !!!");

        return series.to();
    }

    @Cacheable(key = "'series::' + #seriesId")
    public SeriesResponse getSeriesById(Long seriesId) throws NoSeriesFoundException {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series found with provide Id !!!"))
                .to();
    }

    public SeriesResponse updateSeries(SeriesCreateRequest seriesCreateRequest, Long seriesId) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series found with provided Id !!!"));
        series.setName(seriesCreateRequest.getName());
        series.setGenre(seriesCreateRequest.getGenre().toString());
        series.setDescription(seriesCreateRequest.getDescription());
        series.setPrice(seriesCreateRequest.getPrice());
        series = seriesRepository.save(series);

        cacheRepository.clearCacheBySeriesId(series.getId());
        cacheRepository.clearCacheBySeriesGenre(series.getGenre());

        /**                                  -----------------------------
         *  SeriesCreationMessage ======>>> | SeriesUpdationMessageTopic |
         *                                  -----------------------------
         */
        streamBridge.send("SeriesUpdationMessageTopic", SeriesCreationMessage.builder()
                .id(series.getId())
                .name(series.getName())
                .genre(series.getGenre())
                .description(series.getDescription())
                .searchableDescription(series.getDescription().toLowerCase())
                .posterURL(
                        StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + series.getUniquePosterId()
                )
                .price(series.getPrice())
                .rating(series.getRating() == null ? -1 : series.getRating())
                .createdAt(series.getCreatedAt())
                .build());

        return series.to();
    }

    public Boolean deleteSeries(Long seriesId) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                        .orElseThrow(() -> new NoSeriesFoundException("No series found with provided id !!!"));

        /**                                  ------------------------------
         *  EpisodeCreationMessage ======>>>| EpisodeDeletionMessageTopic |
         *                                  ------------------------------
         */
        series.getEpisode().parallelStream()
                        .forEach( episode ->
                                streamBridge.send("EpisodeDeletionMessageTopic", episode.getUniquePosterId())
                        );

        /**                    -----------------------------
         *  seriesId ======>>>| SeriesDeletionMessageTopic |
         *                    -----------------------------
         */
        streamBridge.send("SeriesDeletionMessageTopic", seriesId);

        cacheRepository.clearCacheBySeriesId(series.getId());
        cacheRepository.clearCacheBySeriesGenre(series.getGenre());
        cacheRepository.clearCacheBySeriesReview(series.getId());

        seriesRepository.deleteById(seriesId);
        return true;
    }

    @Cacheable(
            cacheNames = "'latest_series::' + #genre",
            key = "'series::' + #genre + '::' + #pageRequest.getPageNumber() + '::' + #pageRequest.getPageSize()"
    )
    public List<SeriesResponse> getNewReleaseSeriesByGenre(Genre genre, Pageable pageRequest) {
        return seriesRepository.findByNewReleaseSeriesByGenre(genre.toString(), pageRequest)
                .stream()
                .map(x -> x.to()).collect(Collectors.toList());
    }

    // TODO:: Revisit for caching
    public String assignSeriesToViewer(Viewer viewer, Long seriesId) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series present with provided movie id !!!"));

        if(userAlreadyHas(series, viewer)) throw new NoSeriesFoundException("This series is not available for you to buy !!!");

        seriesRepository.updateSeriesViewerMapping(seriesId, viewer.getId());

        /**                             ------------------------
         *  SeriesBuyMessage ======>>> | SeriesBuyMessageTopic |
         *                             ------------------------
         */
        streamBridge.send("SeriesBuyMessageTopic", SeriesBuyMessage.builder()
                        .userId(viewer.getUser().getId())
                        .seriesId(seriesId)
                        .isNew(true)
                        .build());

        notificationService.notifyUsers(PrivateMessageRequest.builder()
                .userIdList(Collections.singletonList(viewer.getUser().getId()))
                .content("You have bought series: " + series.getName()
                        + ". \nThanks for the purchase " + viewer.getName()).build());

        return "Bought series successfully !!!";
    }

    private boolean userAlreadyHas(Series series, Viewer viewer) {
        return viewer.getPurchasedSeries()
                .parallelStream().filter(ser -> ser.getId().equals(series.getId()))
                .count() > 0;
    }

    public ReviewResponse reviewSeries(Long seriesId, ReviewCreateRequest reviewCreateRequest) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No Series found with given id !!!"));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        if(userAlreadyHas(series, viewer)) {
            Review review = Review.builder()
                    .viewer(viewer)
                    .comment(reviewCreateRequest.getComment())
                    .rating(reviewCreateRequest.getRating())
                    .reviewForSeries(series).build();
            review = reviewRepository.save(review);

            double newAvgRating = seriesRepository.findAverageRating(seriesId);
            series.setRating(newAvgRating);
            series = seriesRepository.save(series);

            cacheRepository.clearCacheBySeriesId(series.getId());
            cacheRepository.clearCacheBySeriesGenre(series.getGenre());
            cacheRepository.clearCacheBySeriesReview(series.getId());

            /**                                  -----------------------------
             *  SeriesCreationMessage ======>>> | SeriesUpdationMessageTopic |
             *                                  -----------------------------
             */
            streamBridge.send("SeriesUpdationMessageTopic", SeriesCreationMessage.builder()
                    .id(series.getId())
                    .name(series.getName().toLowerCase())
                    .genre(series.getGenre())
                    .description(series.getDescription())
                    .searchableDescription(series.getDescription().toLowerCase())
                    .posterURL(
                            StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + series.getUniquePosterId()
                    )
                    .price(series.getPrice())
                    .rating(series.getRating() == null ? -1 : series.getRating())
                    .createdAt(series.getCreatedAt())
                    .build());

            return review.to();
        }

        throw new NoSeriesFoundException("This Series is not in list of user !!!");
    }

    @Cacheable(
            cacheNames = "'series_review::' + #seriesId",
            key = "'series::rating::' + #seriesId + '::' + #pageRequest.getPageNumber() + '::' + #pageRequest.getPageSize()"
    )
    public List<ReviewResponse> getReviewsOfMovie(Long seriesId, Pageable pageRequest) throws NoMovieFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!!"));
        return reviewRepository.findByReviewForSeries(series, pageRequest).get()
                .stream().map(Review::to).collect(Collectors.toList());
    }

    public List<SeriesResponse> getAllBoughtSeries() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        
        return viewer.getPurchasedSeries()
                .stream()
                .map(Series::to)
                .collect(Collectors.toList());
    }


    public List<SeriesResponse> getAllboughtSeriesByUserId(Long userId) throws NoUserFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFoundException("No User Found By Given id !!!"));
        Viewer viewer = user.getViewer();

        if(viewer == null) throw new NoUserFoundException("User is not a Viewer !!!");

        return viewer.getPurchasedSeries()
                .stream()
                .map(Series::to)
                .collect(Collectors.toList());
    }
}
