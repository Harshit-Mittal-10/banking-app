package com.bank.banking_app.service;

import com.bank.banking_app.dto.*;
import com.bank.banking_app.entity.User;
import com.bank.banking_app.repository.UserRepository;
import com.bank.banking_app.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;



    public BankResponse createAccount(UserRequest userRequest)
    {
        //checking if account is already existing corresponding to that emailId

        if(userRepository.existsByEmail(userRequest.getEmail()))
        {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE).
                    responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();

        }
        User newUser = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName()).gender(userRequest.getGender())
                .address(userRequest.getAddress()).stateOfOrigin(userRequest.getStateOfOrigin()).email(userRequest.getEmail()).phoneNo(userRequest.getPhoneNo())
                .status("Active").accountNumber(AccountUtils.generateAccountNumber()).accountBalance(BigDecimal.valueOf(0)).build();

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder().recipient(savedUser.getEmail()).
        subject("ACCOUNT CREATION").
        messageBody("Congratulation! Your Account has been created Successfully. \nYour Account Details: \n" +
                "Account Name: " + savedUser.getFirstName()+" "+savedUser.getLastName() + "\nAccount Number: " + savedUser.getAccountNumber() + "\nAccount Balace:" + savedUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS).responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE).
                accountInfo(AccountInfo.builder().accountBalance(savedUser.getAccountBalance().toString()).
                        accountNumber(savedUser.getAccountNumber().toString()).
                        accountName(savedUser.getFirstName()+" "+savedUser.getLastName()).build()).build();
    }

    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest)
    {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExist)
        {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }
        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder().responseMessage(AccountUtils.ACCOUNT_FOUND_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                         .accountInfo(AccountInfo.builder().accountName(user.getFirstName() + " " + user.getLastName()).
                                 accountBalance(user.getAccountBalance().toString()).accountNumber(user.getAccountNumber()).build())
                .build();

    }

    public String nameEnquiry(EnquiryRequest enquiryRequest)
    {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExist)
        {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return user.getFirstName() + " " + user.getLastName();
    }

    public BankResponse creditAccount(CreditDebitRequest transactionRequest)
    {
        boolean isAccountExist = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());

        if(!isAccountExist)
        {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }

        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(user);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(user.getAccountNumber()).
        transactionType("CREDIT")
                .amount(transactionRequest.getAmount().toString())
                .build();

        transactionService.saveTransaction(transactionDTO);


        return BankResponse.builder().responseMessage(AccountUtils.AMOUNT_CREDIT_MESSAGE).responseCode(AccountUtils.AMOUNT_CREDIT_CODE)
                .accountInfo(AccountInfo.builder().accountName(user.getFirstName()+" "+user.getLastName()).accountNumber(user.getAccountNumber()).
                        accountBalance(user.getAccountBalance().toString()).build()).build();
    }

    public BankResponse debitAccount(CreditDebitRequest transactionRequest)
    {
        boolean isAccountExist = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());

        if(!isAccountExist)
        {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }

        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        int availableBalance = user.getAccountBalance().intValue();
        int requestedAmount = transactionRequest.getAmount().intValue();
       // if(transactionRequest.getAmount().subtract(user.getAccountBalance()) > 0)

        if(availableBalance < requestedAmount)
        {
            return BankResponse.builder().responseCode(AccountUtils.AMOUNT_DEBIT_AMOUNT_EXCEEDED_BALANCE_CODE)
                    .responseMessage(AccountUtils.AMOUNT_DEBIT_AMOUNT_EXCEEDED_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder().accountNumber(user.getAccountNumber())
                            .accountName(user.getFirstName()+" "+user.getLastName())
                            .accountBalance(user.getAccountBalance().toString()).build()).build();
        }
        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));
        userRepository.save(user);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(user.getAccountNumber())
                .transactionType("DEBIT")
                .amount(transactionRequest.getAmount().toString())
                .build();

        transactionService.saveTransaction(transactionDTO);

        return BankResponse.builder().responseCode(AccountUtils.AMOUNT_DEBIT_CODE)
                .responseMessage(AccountUtils.AMOUNT_DEBIT_MESSAGE)
                .accountInfo(AccountInfo.builder().accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName()+" "+user.getLastName())
                        .accountBalance(user.getAccountBalance().toString()).build()).build();

    }

}
