package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByTransactionId(String transactionId);
    // Define any custom query methods if needed
}
