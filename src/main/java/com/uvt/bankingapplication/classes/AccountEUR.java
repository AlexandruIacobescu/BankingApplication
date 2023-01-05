package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import com.uvt.bankingapplication.exceptions.RetrieveException;
import com.uvt.bankingapplication.interfaces.Transfer;

public class AccountEUR extends Account implements Transfer {
    public AccountEUR(String accountNumber, double amount) throws DeposeException, IllegalAccountException {
        super(accountNumber, amount);
    }

    public double getInterest() {
        return 0.01;
    }

    @Override
    public String toString() {
        return "Account EUR [" + super.toString() + "]";
    }

    @Override
    public void transferTo(Account c, double s) throws RetrieveException {
        super.retrieve(s);
        try{
            c.depose(s);
        } catch(DeposeException e){
            e.printStackTrace();
        }
    }
}
