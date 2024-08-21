package com.payment.service.service;

import com.payment.service.repository.PaymentCacheRepository;
import com.payment.service.request.PaymentRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.commonDTO.TransactionDetails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@EnableCaching
@CacheConfig(cacheNames = "payment_cache_repository", cacheManager = "customPaymentCacheManager")
public class PaymentService {

    @Autowired
    private PaymentCacheRepository cacheRepository;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private StreamBridge streamBridge;

    public String createTransaction(@Valid PaymentRequest paymentRequest) {

        TransactionDetails transactionDetails = cacheRepository.createTransaction(UUID.randomUUID().toString(), paymentRequest);
       RazorpayClient razorpayClient = null; Order order = null;
        try {
            razorpayClient = new RazorpayClient(keyId, keySecret);

            JSONObject options = new JSONObject();
            options.put("amount", transactionDetails.getPaidAmount()*100);
            options.put("currency", "INR");
            options.put("receipt", transactionDetails.getTransactionId());

            order = razorpayClient.Orders.create(options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return order.toString();
    }

    public void sendTransaction(String transactionId) {
        TransactionDetails transactionDetails = cacheRepository.getTransactionDetailsById(transactionId);
        if(transactionDetails != null) {
            cacheRepository.clearTransactionCache(transactionDetails.getTransactionId());
            /**                               --------------------------
             *  TransactionDetails ======>>> | TransactionDetailsTopic |
             *                               --------------------------
             */
            streamBridge.send("TransactionDetailsTopic", transactionDetails);
        }
    }

}
