package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Movie;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.MovieCreateRequest;
import com.Core_Service.model_response.MovieResponse;
import com.Core_Service.repository.MovieRepository;
import org.commonDTO.MovieBuyMessage;
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

    public MovieResponse addMovie(MovieCreateRequest movieCreateRequest){
        Movie movie = Movie.builder().name(movieCreateRequest.getName())
                .genre(movieCreateRequest.getGenre().toString()).description(movieCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(movieCreateRequest.getPrice())
                .build();
        return movieRepository.save(movie).to();
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

        if(userAlreadyHave(movie, viewer)) throw new NoMovieFoundException("This movie is not available for you to buy !!!");

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

    private boolean userAlreadyHave(Movie movie, Viewer viewer) {
        long viewerId = viewer.getId();
        List<Viewer> viewers = movie.getViewers().parallelStream()
                .filter(v -> v.getId() == viewerId)
                .collect(Collectors.toList());
        return !viewers.isEmpty();
    }
}
