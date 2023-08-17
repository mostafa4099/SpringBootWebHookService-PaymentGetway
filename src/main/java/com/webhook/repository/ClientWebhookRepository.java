package com.webhook.repository;

import com.webhook.entity.ClientWebhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.repository.ClientWebhook.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 10:51 AM
 */
public interface ClientWebhookRepository extends JpaRepository<ClientWebhook, Long> {
    ClientWebhook getClientWebhookByMerchantNumber(String merchantNumber);
}
