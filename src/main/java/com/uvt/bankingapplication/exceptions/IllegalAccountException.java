package com.uvt.bankingapplication.exceptions;

public class IllegalAccountException extends Exception{
    public IllegalAccountException(String exceptionMessage){
        super(exceptionMessage);
    }
}
