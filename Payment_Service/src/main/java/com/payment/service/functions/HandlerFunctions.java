package com.payment.service.functions;

import com.payment.service.models.Movie;
import com.payment.service.models.Series;
import com.payment.service.request.PaymentRequest;
import com.payment.service.service.MovieService;
import com.payment.service.service.PaymentService;
import com.payment.service.service.SeriesService;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class HandlerFunctions {

    @Autowired
    private MovieService movieService;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private PaymentService paymentService;

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

    @Bean
    public Function<PaymentRequest, String> createTransaction() {
        return request -> paymentService.createTransaction(request);
    }

    @Bean
    public Consumer<String> publishTransaction() {
        return transactionId -> paymentService.sendTransaction(transactionId);
    }

}
