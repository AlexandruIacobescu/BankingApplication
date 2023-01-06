package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.ExistingAccountException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void createMoreClientsWithTheSameAccountTest() throws DeposeException, IllegalAccountException {
        Bank bank = Bank.getInstance("UniCredit Bank", (msg, client) -> {});

        Client client1 = new Client("Anna Holt", "42nd Downing Street", Account.TYPE.RON, "1234-5678-9101-1213", "IE12BOFI90000112345678", 250, (msg, client) -> {

        });
        Client client2 = new Client("Madison Leigh", "42nd Downing Street", Account.TYPE.RON, "1234-5678-9101-1213","IE12BOFI90000112345678",  250, (msg, client) -> {

        });
        ExistingAccountException exception = Assertions.assertThrows(ExistingAccountException.class, () -> {
            bank.addClient(client1);
            bank.addClient(client2);
        });
        Assertions.assertEquals("This account number is already owned by another client : " + "1234-5678-9101-1213", exception.getMessage());
    }
}