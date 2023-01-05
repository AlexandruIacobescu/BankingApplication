package com.uvt.bankingapplication.classes.decorators;

import com.uvt.bankingapplication.classes.Account;

public abstract class AccountDecorator extends Account {
    Account account;

    protected AccountDecorator(Account account) {
        this.account = account;
    }
}
