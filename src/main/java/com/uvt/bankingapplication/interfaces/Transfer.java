package com.uvt.bankingapplication.interfaces;

import com.uvt.bankingapplication.classes.Account;
import com.uvt.bankingapplication.exceptions.RetrieveException;

public interface Transfer {
    void transferTo(Account c, double s) throws RetrieveException;
}
