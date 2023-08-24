package com.webhook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.entity.Transaction.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 10:42 AM
 */
@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantNumber;
    private String transactionId;
    private String transactionNumber;
    private double amount;
    private LocalDateTime transactionDateTime;
    private String comment;
    private String status;
}
