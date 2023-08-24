package com.webhook.repository;

import com.webhook.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Md. Golam Mostafa | mostafa.sna@gmail.com
 * @File com.webhook.repository.TransactionRepository.java: SpringBootWebHookService-PaymentGetway
 * @CreationDate 8/17/2023 10:53 AM
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByStatus(String status);
}
