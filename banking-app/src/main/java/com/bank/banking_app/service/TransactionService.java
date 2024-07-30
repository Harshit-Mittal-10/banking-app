package com.bank.banking_app.service;

import com.bank.banking_app.dto.TransactionDTO;
import com.bank.banking_app.entity.Transaction;
import com.bank.banking_app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public void saveTransaction(TransactionDTO transactionDTO)
    {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .accountNumber(transactionDTO.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .status(("SUCCCESS"))
                .build();
        transactionRepository.save(transaction);
    }
}
