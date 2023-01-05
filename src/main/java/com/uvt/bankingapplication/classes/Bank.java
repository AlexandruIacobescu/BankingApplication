package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.exceptions.ExistingAccountException;
import com.uvt.bankingapplication.interfaces.Mediator;

import java.util.ArrayList;
import java.util.List;

public class Bank extends Entity {

    private static volatile Bank instance;

    public static Bank getInstance(String bankCode, Mediator m){
        Bank result = instance;
        if(result == null){
            synchronized (Bank.class){
                if(result == null){
                    instance = result = new Bank(bankCode, m);
                }
            }
        }
        return instance;
    }
    private List<Client> clients;
    private String bankCode = null;

    private Bank(String bankCode, Mediator m) {
        super(m);
        this.bankCode = bankCode;
        clients = new ArrayList<>();
    }

    public void addClient(Client c) throws ExistingAccountException {
        // TODO use a set
        for(var client : clients){
            for(var account : client.getAccounts()){
                for(var account1 : c.getAccounts()){
                    if(account.getAccountNumber().equals(account1.getAccountNumber()))
                        throw new ExistingAccountException("This account number is already owned by another client : " + account.getAccountNumber());
                }
            }
        }
        clients.add(c);
    }

    public Client getClient(String name) {
        for (var client : clients) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return "Bank [code=" + bankCode + ", clients=" + clients + "]";
    }

    public void receive(String msg) {
        System.out.println("Message from client received: " + msg);
    }
}
