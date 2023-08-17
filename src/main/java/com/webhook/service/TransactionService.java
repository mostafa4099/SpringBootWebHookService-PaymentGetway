package com.webhook.service;

import com.webhook.entity.Transaction;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.service.TransactionService.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 10:59 AM
 */
public interface TransactionService {
    String saveTransactionAndSendWebhook(Transaction transaction);
}
