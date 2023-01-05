package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.classes.Account;
import com.uvt.bankingapplication.classes.ApplicationMediator;
import com.uvt.bankingapplication.classes.Bank;
import com.uvt.bankingapplication.classes.Client;
import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    public void bankClientCommunicationTest() throws DeposeException, IllegalAccountException {
        ApplicationMediator mediator = new ApplicationMediator();
        Bank bank = Bank.getInstance("NewBank", mediator);

        Client client1 = new Client.ClientBuilder()
                .name("Anna Gunn")
                .address("44th Downing Street")
                .dateOfBirth("2001-09-06")
                .addAccount(Account.TYPE.RON, "9874-2558-6321-2011", "IE12BOFI90000112345678", 620)
                .mediator(mediator)
                .clientCode("dfsdfsdff")
                .clientPassword("wedfedfgfgh")
                .build();

        mediator.addClient(bank);
        mediator.addClient(client1);

        bank.send("Hello message");
        client1.send("Hello Response");
    }

}