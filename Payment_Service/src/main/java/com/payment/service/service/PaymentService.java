package com.payment.service.service;

import com.payment.service.repository.PaymentCacheRepository;
import com.payment.service.request.PaymentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentCacheRepository cacheRepository;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // TODO:: Continue from here
    public String createTransaction(@Valid PaymentRequest paymentRequest) {
        return null;
    }

}
