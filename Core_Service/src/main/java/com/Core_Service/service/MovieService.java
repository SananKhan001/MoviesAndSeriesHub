package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model.Movie;
import com.Core_Service.model.Review;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.MovieCreateRequest;
import com.Core_Service.model_request.ReviewCreateRequest;
import com.Core_Service.model_response.MovieResponse;
import com.Core_Service.model_response.ReviewResponse;
import com.Core_Service.repository.MovieRepository;
import com.Core_Service.repository.ReviewRepository;
import org.commonDTO.MovieBuyMessage;
import org.commonDTO.MovieCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private ReviewRepository reviewRepository;

    public MovieResponse addMovie(MovieCreateRequest movieCreateRequest){
        Movie movie = Movie.builder().name(movieCreateRequest.getName())
                .genre(movieCreateRequest.getGenre().toString()).description(movieCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(movieCreateRequest.getPrice())
                .build();
        movie = movieRepository.save(movie);

        /**                                 ----------------------------
         *  MovieCreationMessage ======>>> | MovieCreationMessageTopic |
         *                                 ----------------------------
         */
        streamBridge.send("MovieCreationMessageTopic", MovieCreationMessage.builder()
                .id(movie.getId())
                .name(movie.getName())
                .genre(movie.getGenre())
                .description(movie.getDescription())
                .searchableDescription(movie.getDescription().toLowerCase())
                .posterURL(
                        StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + movie.getUniquePosterId()
                )
                .price(movie.getPrice())
                .rating(movie.getRating() == null ? -1 : movie.getRating())
                .createdAt(movie.getCreatedAt())
                .build());

        return movie.to();
    }

    public MovieResponse updateMovie(MovieCreateRequest movieCreateRequest, Long id) throws NoMovieFoundException {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!"));
        movie.setName(movieCreateRequest.getName());
        movie.setGenre(movieCreateRequest.getGenre().toString());
        movie.setDescription(movieCreateRequest.getDescription());
        movie.setPrice(movieCreateRequest.getPrice());
        return movieRepository.save(movie).to();
    }

    public Boolean deleteMovie(Long movieId) throws NoMovieFoundException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!"));

        /**                          ------------------------------
         *  uniquePosterId ======>>>| EpisodeDeletionMessageTopic |
         *                          ------------------------------
         */
        if(movie.getEpisode() != null) streamBridge.send("EpisodeDeletionMessageTopic", movie.getEpisode().getUniquePosterId());

        /**                   ----------------------------
         *  movieId ======>>>| MovieDeletionMessageTopic |
         *                   ----------------------------
         */
        streamBridge.send("MovieDeletionMessageTopic", movieId);

        movieRepository.delete(movie);
        return true;
    }

    public MovieResponse getMovieById(Long movieId) throws NoMovieFoundException {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!"))
                .to();
    }

    public List<MovieResponse> getNewReleaseMoviesByGenre(String genre, Pageable pageRequest) {
        return movieRepository.findNewReleaseMoviesByGenre(genre, pageRequest)
                .stream()
                .map(Movie::to).collect(Collectors.toList());
    }

    public String assignMovieToCurrentUser(Long movieId) throws NoMovieFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No movie present with provided movie id !!!"));

        if(userAlreadyHas(movie, viewer)) throw new NoMovieFoundException("This movie is not available for you to buy !!!");

        movie.getViewers().add(viewer);
        movie = movieRepository.save(movie);

        /**                            -----------------------
         *  MovieBuyMessage ======>>> | MovieBuyMessageTopic |
         *                            -----------------------
         */
        streamBridge.send("MovieBuyMessageTopic", MovieBuyMessage.builder()
                .userId(user.getId())
                .movieId(movieId)
                .isNew(true)
                .build());

        return "Bought movie successfully !!!";
    }

    private boolean userAlreadyHas(Movie movie, Viewer viewer) {
        return viewer.getPurchasedMovies()
                .parallelStream()
                .filter(m -> m.getId().equals(movie.getId()))
                .count() > 0;
    }

    public ReviewResponse reviewMovie(Long movieId, ReviewCreateRequest reviewCreateRequest) throws NoMovieFoundException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No Movie found with given id !!!"));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        if(userAlreadyHas(movie, viewer)) {
            Review review = Review.builder()
                    .viewer(viewer)
                    .comment(reviewCreateRequest.getComment())
                    .rating(reviewCreateRequest.getRating())
                    .reviewForMovie(movie).build();
            review = reviewRepository.save(review);

            double newAvgRating = movieRepository.findAverageRating(movieId);
            movie.setRating(newAvgRating);
            movie = movieRepository.save(movie);

            /**                                 ----------------------------
             *  MovieCreationMessage ======>>> | MovieUpdationMessageTopic |
             *                                 ----------------------------
             */
            streamBridge.send("MovieUpdationMessageTopic", MovieCreationMessage.builder()
                    .id(movie.getId())
                    .name(movie.getName().toLowerCase())
                    .genre(movie.getGenre())
                    .description(movie.getDescription())
                    .searchableDescription(movie.getDescription().toLowerCase())
                    .posterURL(
                            StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + movie.getUniquePosterId()
                    )
                    .price(movie.getPrice())
                    .rating(movie.getRating() == null ? -1 : movie.getRating())
                    .createdAt(movie.getCreatedAt())
                    .build());

            return review.to();
        }

        throw new NoMovieFoundException("This Movie is not in list of user !!!");
    }

    public List<ReviewResponse> getReviewsOfMovie(Long movieId, Pageable pageRequest) throws NoMovieFoundException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!!"));
        return reviewRepository.findByReviewForMovie(movie, pageRequest).get()
                .stream().map(review -> review.to()).collect(Collectors.toList());
    }
}
