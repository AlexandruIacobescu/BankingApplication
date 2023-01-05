package com.uvt.bankingapplication.interfaces;

import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.RetrieveException;

public interface Operations {
    double getTotalAmount();
    double getInterest();
    void depose(double amount) throws DeposeException;
    void retrieve(double amount) throws RetrieveException;
}
