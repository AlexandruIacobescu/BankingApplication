package com.uvt.bankingapplication.classes.commanders;

import com.uvt.bankingapplication.classes.Account;
import com.uvt.bankingapplication.exceptions.RetrieveException;
import com.uvt.bankingapplication.interfaces.Command;

public class RetrieveCommand implements Command {
    private Account account;
    private double amount;

    public RetrieveCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws RetrieveException {
        account.retrieve(amount);
    }
}
