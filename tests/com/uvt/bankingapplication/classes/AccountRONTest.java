package com.uvt.bankingapplication.classes;


import com.uvt.bankingapplication.classes.AccountRON;
import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import com.uvt.bankingapplication.exceptions.RetrieveException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountRONTest {

    @Test
    public void createAccountRONWithNegativeAmount(){
        double amount = -98.0;
        IllegalAccountException exception = Assertions.assertThrows(IllegalAccountException.class, () -> {
            AccountRON account = new AccountRON("0000-0000-0000-0000", "IE12BOFI90000112345678", amount);
        });
        Assertions.assertEquals("Cannot create an account with a negative balance : " + amount, exception.getMessage());
    }

    @Test
    public void attemptToRetrieveMoreThanTheCurrentBalanceFromAccountRON(){
        double instanceAmount = 50;
        double retrieveAmount = 100;
        RetrieveException exception = Assertions.assertThrows(RetrieveException.class, () -> {
            AccountRON account = new AccountRON("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmount);
            account.retrieve(retrieveAmount);
        });
        Assertions.assertEquals("Cannot retrieve more than the current balance : " + retrieveAmount, exception.getMessage());
    }

    @Test
    public void attemptToRetrieveANegativeAmountFromAccountRON(){
        double instanceAmount = 500;
        double amount = -100;
        RetrieveException exception = Assertions.assertThrows(RetrieveException.class, () -> {
            AccountRON account = new AccountRON("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmount);
            account.retrieve(amount);
        });
        Assertions.assertEquals("Cannot retrieve a negative amount : " + amount, exception.getMessage());
    }

    @Test
    public void getTotalAmountTestFromAccountRON() throws DeposeException, IllegalAccountException {
        double requiredTotalAmountForUnder500 = 412;
        double requiredTotalAmountForOver500 = 648;
        double instanceAmountUnder500 = 400;
        double instanceAmountOver500 = 600;

        AccountRON account1 = new AccountRON("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmountOver500);
        AccountRON account2 = new AccountRON("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmountUnder500);
        Assertions.assertEquals(account1.getTotalAmount(), requiredTotalAmountForOver500);
        Assertions.assertEquals(account2.getTotalAmount(), requiredTotalAmountForUnder500);
    }

    @Test
    public void AccountRONTransferTest() throws DeposeException, IllegalAccountException, RetrieveException {
        AccountRON account1 = new AccountRON("0000-0000-0000-0000", "IE12BOFI90000112345678", 500);
        AccountRON account2 = new AccountRON("0000-0000-0000-0001", "IE12BOFI90000112345678", 1000);

        account1.transferTo(account2, 200);
        Assertions.assertEquals(1200 + 1200 * 0.08, account2.getTotalAmount());
        Assertions.assertEquals(300 + 300 * 0.03, account1.getTotalAmount());
    }
}