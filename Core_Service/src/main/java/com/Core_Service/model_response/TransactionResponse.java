package com.Core_Service.model_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private String contentName;
    private String contentType;
    private int paidAmount;
    private String viewerName;
    private String createdAt;
}
