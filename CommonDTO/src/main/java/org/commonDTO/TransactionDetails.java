package org.commonDTO;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails {
    private String TransactionId;
    private String contentType; // Movie or Series
    private String contentId;
    private String viewerId;
    private String contentName;
    private int paidAmount;
}
