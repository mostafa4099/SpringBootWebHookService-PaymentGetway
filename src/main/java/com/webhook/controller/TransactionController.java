package com.webhook.controller;

import com.webhook.entity.Transaction;
import com.webhook.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.controller.TransectionController.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 10:55 AM
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/simulate")
    public String simulateTransaction(@RequestBody Transaction transaction) {

        String message = transactionService.saveTransactionAndSendWebhook(transaction);

        return message;
    }
}
