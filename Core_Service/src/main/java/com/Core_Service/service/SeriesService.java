package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.helpers.Helper;
import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model.*;
import com.Core_Service.model_request.ReviewCreateRequest;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.ReviewResponse;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.repository.ReviewRepository;
import com.Core_Service.repository.SeriesRepository;
import org.commonDTO.MovieCreationMessage;
import org.commonDTO.SeriesBuyMessage;
import org.commonDTO.SeriesCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private ReviewRepository reviewRepository;

    public SeriesResponse addSeries(SeriesCreateRequest seriesCreateRequest) {
        Series series = Series.builder().name(seriesCreateRequest.getName())
                .genre(seriesCreateRequest.getGenre().toString())
                .description(seriesCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(seriesCreateRequest.getPrice()).build();
        series = seriesRepository.save(series);

        /**                                  -----------------------------
         *  SeriesCreationMessage ======>>> | SeriesCreationMessageTopic |
         *                                  -----------------------------
         */
        streamBridge.send("SeriesCreationMessageTopic", SeriesCreationMessage.builder()
                .id(series.getId())
                .name(series.getName().toLowerCase())
                .genre(series.getGenre())
                .description(series.getDescription().toLowerCase())
                .posterURL(
                        StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + series.getUniquePosterId()
                )
                .price(series.getPrice())
                .rating(series.getRating() == null ? -1 : series.getRating())
                .createdAt(series.getCreatedAt())
                .build());

        return series.to();
    }

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
        return seriesRepository.save(series).to();
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

        seriesRepository.deleteById(seriesId);
        return true;
    }
    public List<SeriesResponse> getNewReleaseSeriesByGenre(Genre genre, Pageable pageRequest) {
        return seriesRepository.findByNewReleaseSeriesByGenre(genre.toString(), pageRequest)
                .stream()
                .map(x -> x.to()).collect(Collectors.toList());
    }

    public String assignSeriesToCurrentUser(Long seriesId) throws NoSeriesFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series present with provided movie id !!!"));

        if(userAlreadyHas(series, viewer)) throw new NoSeriesFoundException("This series is not available for you to buy !!!");

        series.getViewers().add(viewer);
        seriesRepository.save(series);

        /**                             ------------------------
         *  SeriesBuyMessage ======>>> | SeriesBuyMessageTopic |
         *                             ------------------------
         */
        streamBridge.send("SeriesBuyMessageTopic", SeriesBuyMessage.builder()
                        .userId(user.getId())
                        .seriesId(seriesId)
                        .isNew(true)
                        .build());

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

            /**                                  -----------------------------
             *  SeriesCreationMessage ======>>> | SeriesUpdationMessageTopic |
             *                                  -----------------------------
             */
            streamBridge.send("SeriesUpdationMessageTopic", SeriesCreationMessage.builder()
                    .id(series.getId())
                    .name(series.getName().toLowerCase())
                    .genre(series.getGenre())
                    .description(series.getDescription().toLowerCase())
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

    public List<ReviewResponse> getReviewsOfMovie(Long seriesId, Pageable pageRequest) throws NoMovieFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!!"));
        return reviewRepository.findByReviewForSeries(series, pageRequest).get()
                .stream().map(review -> review.to()).collect(Collectors.toList());
    }
}
