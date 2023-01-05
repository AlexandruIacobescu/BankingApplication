package com.uvt.bankingapplication.exceptions;

public class ExistingAccountException extends Exception {
    public ExistingAccountException(String exceptionMessage){
        super(exceptionMessage);
    }
}
