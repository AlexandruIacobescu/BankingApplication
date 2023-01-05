package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.interfaces.Mediator;

import java.util.ArrayList;

public class ApplicationMediator implements Mediator {
    private ArrayList<Entity> clients;

    public ApplicationMediator(){
        clients = new ArrayList<>();
    }

    public void addClient(Entity client){
        clients.add(client);
    }

    public void send(String message, Entity originator) {
        for(var client : clients) {
            if(client != originator) {
                client.receive(message);
            }
        }
    }
}
