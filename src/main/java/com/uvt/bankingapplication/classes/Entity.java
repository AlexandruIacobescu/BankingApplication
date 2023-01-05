package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.interfaces.Mediator;

public abstract class Entity {
    private Mediator mediator;

    public Entity(Mediator m){
        mediator = m;
    }

    public void send(String msg){
        mediator.send(msg, this);
    }

    public Mediator getMediator(){
        return mediator;
    }

    public abstract void receive(String msg);
}
