package com.payment.service.repository;

import com.payment.service.enums.ContentType;
import com.payment.service.models.Movie;
import com.payment.service.models.Series;
import com.payment.service.request.PaymentRequest;
import com.payment.service.service.MovieService;
import com.payment.service.service.SeriesService;
import org.commonDTO.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "payment_cache_repository", cacheManager = "customPaymentCacheManager")
public class PaymentCacheRepository {

    @Autowired
    private MovieService movieService;

    @Autowired
    private SeriesService seriesService;

    @CachePut(key = "'payment::' + #transactionId")
    public TransactionDetails createTransaction(String transactionId, PaymentRequest paymentRequest) {
        String contentName = null; Integer amountToPay = null;
        if(ContentType.MOVIE.equals(paymentRequest.getContentType())) {
            Movie movie = movieService.findById(paymentRequest.getContentId());
            contentName = movie.getName();
            amountToPay = movie.getPrice();
        }
        else {
            Series series = seriesService.findById(paymentRequest.getContentId());
            contentName = series.getName();
            amountToPay = series.getPrice();
        }

        return TransactionDetails.builder()
                .transactionId(transactionId)
                .contentType(paymentRequest.getContentType().toString())
                .contentId(paymentRequest.getContentId())
                .userId(paymentRequest.getUserId())
                .contentName(contentName)
                .paidAmount(amountToPay).build();
    }

    @Cacheable(key = "'payment::' + #transactionId")
    public TransactionDetails getTransactionDetailsById(String transactionId) {
        return null;
    }

    @CacheEvict(key = "'payment::' + #transactionId")
    public void clearTransactionCache(String transactionId) {}
}
