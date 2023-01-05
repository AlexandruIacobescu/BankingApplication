package com.uvt.bankingapplication.classes.commanders;

import com.uvt.bankingapplication.classes.Client;
import com.uvt.bankingapplication.exceptions.AccountClosingException;
import com.uvt.bankingapplication.interfaces.Command;

public class CloseAccountCommand implements Command {
    private Client client;
    private String accountNumber;

    public CloseAccountCommand(Client client, String accountNumber) {
        this.client = client;
        this.accountNumber = accountNumber;
    }

    @Override
    public void execute() throws AccountClosingException {
        client.closeAccount(accountNumber);
    }
}
