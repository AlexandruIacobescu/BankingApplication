package com.uvt.bankingapplication.classes;


import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import com.uvt.bankingapplication.exceptions.RetrieveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountEURTest {

    @Test
    public void createAccountEURWithNegativeAmount(){
        double amount = -98.0;
        IllegalAccountException exception = Assertions.assertThrows(IllegalAccountException.class, () -> {
            AccountEUR account = new AccountEUR("0000-0000-0000-0000", "IE12BOFI90000112345678", amount);
        });
        Assertions.assertEquals("Cannot create an account with a negative balance : " + amount, exception.getMessage());
    }

    @Test
    public void attemptToRetrieveMoreThanTheCurrentBalanceFromAccountEUR(){
        double instanceAmount = 50;
        double retrieveAmount = 100;
        RetrieveException exception = Assertions.assertThrows(RetrieveException.class, () -> {
            AccountEUR account = new AccountEUR("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmount);
            account.retrieve(retrieveAmount);
        });
        Assertions.assertEquals("Cannot retrieve more than the current balance : " + retrieveAmount, exception.getMessage());
    }

    @Test
    public void attemptToRetrieveANegativeAmountFromAccountEUR(){
        double instanceAmount = 500;
        double amount = -100;
        RetrieveException exception = Assertions.assertThrows(RetrieveException.class, () -> {
            AccountEUR account = new AccountEUR("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmount);
            account.retrieve(amount);
        });
        Assertions.assertEquals("Cannot retrieve a negative amount : " + amount, exception.getMessage());
    }

    @Test
    public void getTotalAmountTestFromAccountEUR() throws DeposeException, IllegalAccountException {
        double requiredTotalAmount = 600 + 600 * 0.01;
        double instanceAmount = 600;

        AccountEUR account1 = new AccountEUR("0000-0000-0000-0000", "IE12BOFI90000112345678", instanceAmount);
        Assertions.assertEquals(account1.getTotalAmount(), requiredTotalAmount);
    }

    @Test
    public void AccountEURTransferTest() throws DeposeException, IllegalAccountException, RetrieveException {
        AccountEUR account1 = new AccountEUR("0000-0000-0000-0000", "IE12BOFI90000112345678", 500);
        AccountEUR account2 = new AccountEUR("0000-0000-0000-0001", "IE12BOFI90000112345678", 1000);

        account1.transferTo(account2, 200);
        Assertions.assertEquals(1200 + 1200 * 0.01, account2.getTotalAmount());
        Assertions.assertEquals(300 + 300 * 0.01, account1.getTotalAmount());
    }
}