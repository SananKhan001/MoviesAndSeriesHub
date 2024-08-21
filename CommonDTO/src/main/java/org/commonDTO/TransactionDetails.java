package org.commonDTO;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails implements Serializable {
    private String transactionId;
    private String contentType; // Movie or Series
    private Long contentId;
    private Long userId;
    private String contentName;
    private int paidAmount;
}
