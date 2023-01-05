package com.uvt.bankingapplication.classes.commanders;

import com.uvt.bankingapplication.classes.Account;
import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.interfaces.Command;

public class DeposeCommand implements Command {

    private Account account;
    private double amount;

    public DeposeCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws DeposeException {
        account.depose(amount);
    }
}
