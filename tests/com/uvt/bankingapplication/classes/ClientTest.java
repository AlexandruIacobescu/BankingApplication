package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.exceptions.AccountClosingException;
import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void createValidClientWithRONAccountTest() throws DeposeException, IllegalAccountException {
        Client client = new Client("Anna Holt", "42nd Downing Street", Account.TYPE.RON, "1234-5678-9101-1213", 250, (msg, client1) -> {
        });
    }

    @Test
    public void createValidClientWithInvalidRONAccountTest() {
        double amount = -250;
        IllegalAccountException exception = Assertions.assertThrows(IllegalAccountException.class, () -> {
            Client client = new Client("Anna Holt", "42nd Downing Street", Account.TYPE.RON, "1234-5678-9101-1213", amount, (msg, client1) -> {

            });
        });
        Assertions.assertEquals("Cannot create an account with a negative balance : " + amount, exception.getMessage());
    }

    @Test
    public void addAccountToClientTest() throws DeposeException, IllegalAccountException {
        Client client = new Client("Anna Holt", "42nd Downing Street", Account.TYPE.RON, "1234-5678-9101-1213", 250, (msg, client1) -> {

        });
        client.addAccount(Account.TYPE.RON, "1234-5678-9101-1213", 900);
    }

    @Test
    public void createClientUsingTheClientBuilderTest() throws DeposeException, IllegalAccountException {
        Client client1 = new Client.ClientBuilder()
                .name("Anna Gunn")
                .address("44th Downing Street")
                .dateOfBirth("2001-09-06")
                .addAccount(Account.TYPE.RON, "9874-2558-6321-2011", 620)
                .clientCode("BLIZZARD11")
                .clientPassword("Rockefeller")
                .build();

        //System.out.println(client1);
    }

    @Test
    public void closeAccountTest() throws DeposeException, IllegalAccountException, AccountClosingException {
        Client client = new Client("Anna Holt", "42nd Downing Street", Account.TYPE.RON, "1234-5678-9101-1213", 250, (msg, client1) -> {

        });
        client.addAccount(Account.TYPE.RON, "1234-5678-9101-1214", 0);
        client.closeAccount("1234-5678-9101-1214");
    }

}