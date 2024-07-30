package com.bank.banking_app.controller;

import com.bank.banking_app.dto.BankResponse;
import com.bank.banking_app.dto.TransactionDTO;
import com.bank.banking_app.entity.Transaction;
import com.bank.banking_app.service.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bankStatement")
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate,
                                                   @RequestParam String endDate)
    {
        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }
}
