package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Series;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.repository.SeriesRepository;
import org.commonDTO.SeriesBuyMessage;
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

    public SeriesResponse addSeries(SeriesCreateRequest seriesCreateRequest) {
        Series series = Series.builder().name(seriesCreateRequest.getName())
                .genre(seriesCreateRequest.getGenre().toString())
                .description(seriesCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(seriesCreateRequest.getPrice()).build();
        return seriesRepository.save(series).to();
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

        if(userAlreadyHave(series, viewer)) throw new NoSeriesFoundException("This series is not available for you to buy !!!");

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

    private boolean userAlreadyHave(Series series, Viewer viewer) {
        long viewerId = viewer.getId();
        List<Viewer> viewers = series.getViewers().parallelStream()
                .filter(v -> v.getId() == viewerId)
                .collect(Collectors.toList());
        return !viewers.isEmpty();
    }
}
