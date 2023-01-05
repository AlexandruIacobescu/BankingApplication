package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import com.uvt.bankingapplication.exceptions.RetrieveException;
import com.uvt.bankingapplication.interfaces.Transfer;

public class AccountRON extends Account implements Transfer {
    public AccountRON(String accountNumber, String IBAN, double amount) throws DeposeException, IllegalAccountException {
        super(accountNumber, IBAN, amount);
    }

    public double getInterest() {
        if (amount < 500)
            return 0.03;
        else
            return 0.08;

    }

    @Override
    public String toString() {
        return "Account RON [" + super.toString() + "]";
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
