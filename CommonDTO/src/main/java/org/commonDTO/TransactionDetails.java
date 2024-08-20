package org.commonDTO;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails implements Serializable {
    private String TransactionId;
    private String contentType; // Movie or Series
    private String contentId;
    private String userId;
    private String contentName;
    private int paidAmount;
}
