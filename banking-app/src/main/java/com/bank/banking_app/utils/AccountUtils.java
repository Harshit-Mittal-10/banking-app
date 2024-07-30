package com.bank.banking_app.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user Already has a account";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has created successfully!";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided Account Number does not exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found";
    public static final String AMOUNT_CREDIT_CODE = "005";
    public static final String AMOUNT_CREDIT_MESSAGE = "Amount credit successfully";
    public static final String AMOUNT_DEBIT_CODE = "006";
    public static final String AMOUNT_DEBIT_MESSAGE = "Amount debited successfully";
    public static final String AMOUNT_DEBIT_AMOUNT_EXCEEDED_BALANCE_CODE = "007";
    public static final String AMOUNT_DEBIT_AMOUNT_EXCEEDED_BALANCE_MESSAGE = "Account does not have this much money";
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_CODE = "008";
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_MESSAGE = "Source Account does not exist";
    public static final String DESTINATION_ACCOUNT_NOT_EXISTS_CODE = "009";
    public static final String DESTINATION_ACCOUNT_NOT_EXISTS_MESSAGE = "Destination Account does not exist";
    public static final String TRANSFER_SUCCESSFUL_CODE = "010";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Money transfer successful";

    public static String generateAccountNumber() {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generating  a random no now.
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();

    }
}
