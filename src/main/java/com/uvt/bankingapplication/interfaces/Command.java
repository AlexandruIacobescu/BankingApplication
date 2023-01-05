package com.uvt.bankingapplication.interfaces;


import com.uvt.bankingapplication.exceptions.AccountClosingException;
import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.RetrieveException;

public interface Command {
    void execute() throws DeposeException, RetrieveException, AccountClosingException;
}
