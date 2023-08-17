package com.webhook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.entity.Client_Merchant_Info.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 10:37 AM
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientWebhook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String merchantNumber;

    private String clientName;

    private String webhookUrl;
}
