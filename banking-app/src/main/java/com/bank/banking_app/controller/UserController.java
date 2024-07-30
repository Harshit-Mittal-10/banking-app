package com.bank.banking_app.controller;

import com.bank.banking_app.dto.*;
import com.bank.banking_app.service.TransferService;
import com.bank.banking_app.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management APIs")
public class UserController {
     @Autowired
    UserService userService;

     @Autowired
    TransferService transferService;

     @PostMapping("/create")
      public BankResponse createAccount(@RequestBody UserRequest userRequest){
         return userService.createAccount(userRequest);
     }

     @GetMapping("/balanceEnquiry")
     public BankResponse balanceEnquiryRequest(@RequestBody EnquiryRequest enquiryRequest)
     {
         return userService.balanceEnquiry(enquiryRequest);
     }

     @GetMapping("/nameEnquiry")
     public String nameEnquiryRequest(@RequestBody EnquiryRequest enquiryRequest)
     {
         return userService.nameEnquiry(enquiryRequest);
     }

     @PostMapping("/credit")
    public BankResponse creditAmountRequest(@RequestBody CreditDebitRequest transactionRequest)
     {
         return userService.creditAccount(transactionRequest);
     }

     @PostMapping("/debit")
    public BankResponse debitAmountRequest(@RequestBody CreditDebitRequest transactionRequest)
     {
         return userService.debitAccount(transactionRequest);
     }

     @PostMapping("/transfer")
    public BankResponse transferAmountRequest(@RequestBody TransferRequest transferRequest)
     {
         return transferService.transfer(transferRequest);
     }
}
