package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Series;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.EpisodeResponse;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;
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

    public Boolean deleteSeries(Long seriesId) {
        seriesRepository.deleteById(seriesId);
        return true;
    }

    public EpisodeResponse addEpisodeInSeries(String episodeName, Long seriesId) {
    }
}
