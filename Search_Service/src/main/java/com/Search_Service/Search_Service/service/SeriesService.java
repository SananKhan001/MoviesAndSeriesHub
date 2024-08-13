package com.Search_Service.Search_Service.service;

import com.Search_Service.Search_Service.entity.Series;
import com.Search_Service.Search_Service.repository.SeriesRepository;
import org.commonDTO.SeriesCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    public void save(SeriesCreationMessage seriesCreationMessage) {
        Series series = Series.builder()
                .id(seriesCreationMessage.getId()).name(seriesCreationMessage.getName())
                .genre(seriesCreationMessage.getGenre())
                .description(seriesCreationMessage.getDescription())
                .searchableDescription(seriesCreationMessage.getSearchableDescription())
                .posterURL(seriesCreationMessage.getPosterURL())
                .price(seriesCreationMessage.getPrice()).rating(seriesCreationMessage.getRating())
                .createdAt(seriesCreationMessage.getCreatedAt()).build();

        seriesRepository.save(series);
    }

    public void delete(Long id){
        seriesRepository.deleteById(id);
    }

    public Iterable<Series> findAll() {
        return seriesRepository.findAll();
    }
}
