package com.Core_Service.model;

import com.Core_Service.model_response.TransactionResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "content_name", nullable = false)
    private String contentName;

    @Column(name = "paid_amount", nullable = false)
    private int paidAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "viewer_id")
    private Viewer viewer;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date date;

    public TransactionResponse to() {
        return TransactionResponse.builder()
                .id(this.id).transactionId(this.transactionId)
                .contentName(this.contentName).contentType(this.contentType)
                .paidAmount(this.paidAmount).viewerName(this.viewer.getName())
                .createdAt(this.date.toString()).build();
    }
}
