package com.uvt.bankingapplication.interfaces;


import com.uvt.bankingapplication.classes.Entity;

public interface Mediator {
    void send(String msg, Entity client);
}
