package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Series;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.repository.SeriesRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "addSeries")
    public SeriesResponse addSeries(SeriesCreateRequest seriesCreateRequest) {
        Series series = Series.builder().name(seriesCreateRequest.getName())
                .genre(seriesCreateRequest.getGenre().toString())
                .description(seriesCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(seriesCreateRequest.getPrice()).build();
        return seriesRepository.save(series).to();
    }

    @PermitAll
    @QueryMapping(name = "getSeriesById")
    public SeriesResponse getSeriesById(Long seriesId) throws NoSeriesFoundException {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series found with provide Id !!!"))
                .to();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "updateSeries")
    public SeriesResponse updateSeries(SeriesCreateRequest seriesCreateRequest, Long seriesId) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series found with provided Id !!!"));
        series.setName(seriesCreateRequest.getName());
        series.setGenre(seriesCreateRequest.getGenre().toString());
        series.setDescription(seriesCreateRequest.getDescription());
        series.setPrice(seriesCreateRequest.getPrice());
        return seriesRepository.save(series).to();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "deleteSeries")
    public Boolean deleteSeries(Long seriesId) {
        seriesRepository.deleteById(seriesId);
        return true;
    }
    @PermitAll
    @QueryMapping(name = "getNewReleaseSeriesByGenre")
    public List<SeriesResponse> getNewReleaseSeriesByGenre(Genre genre, Pageable pageRequest) {
        return seriesRepository.findByNewReleaseSeriesByGenre(genre.toString(), pageRequest)
                .stream()
                .map(x -> x.to()).collect(Collectors.toList());
    }
}
