package com.bank.banking_app.service;

import com.bank.banking_app.dto.*;
import com.bank.banking_app.entity.User;
import com.bank.banking_app.repository.UserRepository;
import com.bank.banking_app.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    public BankResponse transfer(TransferRequest transferRequest)
    {
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        if(!isSourceAccountExist)
        {
            return BankResponse.builder().responseCode(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_CODE).
                    responseMessage(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
        }
        if(!isDestinationAccountExist)
        {
            return  BankResponse.builder().responseCode(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_CODE).
                    responseMessage(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
        }

        User sourceUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        User destinationUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        int sourceAmount = sourceUser.getAccountBalance().intValue();
        int requestedAmount = transferRequest.getAmount().intValue();

        if(sourceAmount < requestedAmount)
        {
            return BankResponse.builder().responseCode(AccountUtils.AMOUNT_DEBIT_AMOUNT_EXCEEDED_BALANCE_CODE)
                    .responseMessage(AccountUtils.AMOUNT_DEBIT_AMOUNT_EXCEEDED_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder().accountNumber(sourceUser.getAccountNumber())
                            .accountName(sourceUser.getFirstName()+" "+sourceUser.getLastName())
                            .accountBalance(sourceUser.getAccountBalance().toString()).build())
                    .build();
        }

        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceUser);

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .messageBody("AMOUNT "+transferRequest.getAmount().toString()+"has been Debited from your Account and your current balance is "+ sourceUser.getAccountBalance()).
                recipient(sourceUser.getEmail())
                .build();

        emailService.sendEmailAlert(debitAlert);

        destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .messageBody("AMOUNT "+transferRequest.getAmount().toString()+"has been Credited from your Account and your current balance is"+ destinationUser.getAccountBalance()).
                recipient(destinationUser.getEmail())
                .build();

        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE).
        responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE).build();
    }
}
