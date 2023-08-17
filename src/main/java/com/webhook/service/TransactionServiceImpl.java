package com.webhook.service;

import com.webhook.entity.ClientWebhook;
import com.webhook.entity.Transaction;
import com.webhook.repository.ClientWebhookRepository;
import com.webhook.repository.TransactionRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.service.TransactionServiceImpl.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 11:00 AM
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientWebhookRepository clientWebhookRepository;

    @Value("${base64.encrypt.key}")
    String secretKey;

    @PostConstruct
    public void initClientWebhook() {
        List<ClientWebhook> list = new ArrayList<>();

        list = Stream.of(
                new ClientWebhook(0L, "012345678901", "Daraz", "http://localhost:9091/daraz_payment_receiver"),
                new ClientWebhook(0L, "012345678902", "Daraz", "http://localhost:9091/daraz_payment_receiver"),
                new ClientWebhook(0L, "012345678903", "BEP", "http://localhost:9091/bep_payment_receiver"),
                new ClientWebhook(0L, "012345678904", "BEP", "http://localhost:9091/bep_payment_receiver")
        ).toList();

        clientWebhookRepository.saveAll(list);
    }

    @Override
    @Transactional
    public String saveTransactionAndSendWebhook(Transaction transaction) {
        String message = "";
        Random random = new Random();

        transaction.setTransactionId("ABC" + random.nextInt());
        transaction.setTransactionDateTime(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        ClientWebhook clientWebhook = getClientWebhook(savedTransaction.getMerchantNumber());

        if (null != clientWebhook && StringUtils.isNotBlank(clientWebhook.getWebhookUrl())) {
            String payload = String.format(
                    "{\"merchantNumber\": \"%s\", \"transactionId\": \"%s\", \"amount\": %.2f, \"transactionDateTime\": \"%s\", \"comment\": \"%s\"}",
                    savedTransaction.getMerchantNumber(), savedTransaction.getTransactionId(), savedTransaction.getAmount(),
                    savedTransaction.getTransactionDateTime(), savedTransaction.getComment()
            );

            String encryptPayload = encryptPayload(payload);

            // Send the payload to the webhook URL
            // Implement the webhook sending logic using an HTTP client library
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(encryptPayload, headers);

            int maxRetries = 3;
            int retryAttempts = 0;
            while (retryAttempts <= maxRetries) {
                try {
                    new RestTemplate().postForEntity(clientWebhook.getWebhookUrl(), request, String.class);
                    savedTransaction.setSendStatus(true);
                    break;
                } catch (Exception e) {
                    retryAttempts++;
                }
            }

            savedTransaction.setRetryAttempts(retryAttempts);
            transactionRepository.save(savedTransaction);

            if (savedTransaction.isSendStatus()) {
                message = "Transaction saved and send to merchant successfully.";
            } else {
                message = "Transaction saved successfully but failed data sending operation by " + retryAttempts + " attempts.";
            }

            System.out.println("Sending payload to webhook URL: " + clientWebhook.getWebhookUrl());
            System.out.println("Payload: " + payload);
            System.out.println("Encrypt Payload: " + encryptPayload);
        } else {
            message = "Transaction saved successfully but failed data sending operation due to Webhook URL not found for merchant number: " + savedTransaction.getMerchantNumber() + ".";
        }

        return message;
    }

    private ClientWebhook getClientWebhook(String merchantNumber) {
        // Implement logic to fetch the webhook URL based on the merchant number
        // This could involve querying the database or fetching from a configuration
        // Return the webhook URL or null if not found
        return clientWebhookRepository.getClientWebhookByMerchantNumber(merchantNumber);
    }

    private String encryptPayload(String payload) {
        String returnPayload = "";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            returnPayload = Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnPayload;
    }
}
