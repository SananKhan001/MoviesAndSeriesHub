package com.payment.service.service;

import com.payment.service.models.Series;
import com.payment.service.repository.SeriesCacheRepository;
import com.payment.service.repository.SeriesRepository;
import org.commonDTO.MovieCreationMessage;
import org.commonDTO.SeriesCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCaching
@CacheConfig(cacheNames = "payment_series_cache", cacheManager = "customPaymentCacheManager")
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeriesCacheRepository cacheRepository;

    public void save(SeriesCreationMessage seriesCreationMessage) {
        Series series = Series.builder()
                .id(seriesCreationMessage.getId())
                .name(seriesCreationMessage.getName())
                .price(seriesCreationMessage.getPrice()).build();

        seriesRepository.save(series);
        cacheRepository.clearSeriesCache(series.getId());
    }

    public void delete(Long id) {
        seriesRepository.deleteById(id);
        cacheRepository.clearSeriesCache(id);
    }

    public List<Series> findAll(){
        return seriesRepository.findAll();
    }

    @Cacheable(key = "'series_id::' + #id")
    public Series findById(Long id) {
        return seriesRepository.findById(id).get();
    }

}
