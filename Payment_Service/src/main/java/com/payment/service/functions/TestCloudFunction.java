package com.payment.service.functions;

import com.payment.service.models.Movie;
import com.payment.service.models.Series;
import com.payment.service.service.MovieService;
import com.payment.service.service.SeriesService;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class TestCloudFunction {

    @Autowired
    private MovieService movieService;

    @Autowired
    private SeriesService seriesService;

    @Bean
    public Supplier<List<Series>> findAllSeries(){
        return () -> seriesService.findAll();
    }

    @Bean
    public Supplier<List<Movie>> findAllMovie() {
        return () -> movieService.findAll();
    }

    @Bean
    public Function<Long, Movie> findMovieById() {
        return id -> movieService.findById(id);
    }

    @Bean
    public Function<Long, Series> findSeriesById() {
        return id -> seriesService.findById(id);
    }

}
