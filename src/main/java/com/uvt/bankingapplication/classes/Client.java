package com.uvt.bankingapplication.classes;

import com.uvt.bankingapplication.exceptions.AccountClosingException;
import com.uvt.bankingapplication.exceptions.DeposeException;
import com.uvt.bankingapplication.exceptions.IllegalAccountException;
import com.uvt.bankingapplication.interfaces.Mediator;

import java.util.ArrayList;
import java.util.List;

public class Client extends Entity {
    private String name;
    private String address;
    private String dateOfBirth;
    private List<Account> accounts;
    private String clientCode;
    private String clientPassword;

    public Client(String name, String address, Account.TYPE type, String accountNumber, String IBAN, double amount, Mediator m) throws DeposeException, IllegalAccountException {
        super(m);
        this.name = name;
        this.address = address;
        accounts = new ArrayList<>();
        addAccount(type, accountNumber, IBAN, amount);
    }

    public Client(String name, String address, Account.TYPE type, String accountNumber, String IBAN, double amount, String clientCode, String clientPassword) throws DeposeException, IllegalAccountException {
        super((msg, client) -> {});
        this.name = name;
        this.address = address;
        accounts = new ArrayList<>();
        addAccount(type, accountNumber, IBAN, amount);
        this.clientCode = clientCode;
        this.clientPassword = clientPassword;
    }

    public void receive(String msg) {
        System.out.println("Message from bank received: " + msg);
    }

    public static class ClientBuilder {
        private String name;
        private String address;
        private String dateOfBirth;
        private Mediator mediator;
        private String clientCode;
        private String clientPassword;
        private List<Account> accounts = new ArrayList<>();
        public ClientBuilder name(String name){
            this.name = name;
            return this;
        }

        public ClientBuilder address(String address){
            this.address = address;
            return this;
        }

        public ClientBuilder dateOfBirth(String dof){
            this.dateOfBirth = dof;
            return this;
        }

        public ClientBuilder clientCode(String code){
            this.clientCode = code;
            return this;
        }

        public ClientBuilder clientPassword(String password){
            this.clientPassword = password;
            return this;
        }

        public ClientBuilder mediator(Mediator m){
            this.mediator = m;
            return this;
        }

        public ClientBuilder addAccount(Account.TYPE type, String accountNumber, String IBAN, double amount) throws DeposeException, IllegalAccountException {
            Account c = null;
            if (type == Account.TYPE.EUR)
                c = new AccountEUR(accountNumber, IBAN, amount);
            else if (type == Account.TYPE.RON)
                c = new AccountRON(accountNumber, IBAN, amount);
            this.accounts.add(c);
            return this;
        }

        public Client build() {
            if(name == null)
                throw new IllegalStateException("Client has no name.");
            if(address == null)
                throw new IllegalStateException("Client has no address");
            if(clientCode == null)
                throw new IllegalStateException("Client has no client code");
            if(clientPassword == null)
                throw new IllegalStateException("Client has no client password");
            return new Client(this);
        }
    }
    public Client(ClientBuilder builder) {
        super(builder.mediator);
        this.name = builder.name;
        this.address = builder.address;
        this.dateOfBirth = builder.dateOfBirth;
        this.accounts = builder.accounts;
    }

    public void addAccount(Account.TYPE type, String accountNumber, String IBAN, double amount) throws DeposeException, IllegalAccountException {
        Account c = null;
        if (type == Account.TYPE.EUR)
            c = new AccountEUR(accountNumber, "IE12BOFI90000112345678", amount);
        else if (type == Account.TYPE.RON)
            c = new AccountRON(accountNumber, "IE12BOFI90000112345678", amount);
        accounts.add(c);
    }

    public Account getAccount(String accountCode) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountCode)) {
                return account;
            }
        }
        return null;
    }

    public List<Account> getAccounts(){
        List<Account> clonedAccounts = new ArrayList<>();
        for(var account : accounts){
            clonedAccounts.add(account.clone());
        }
        return clonedAccounts;
    }

    public void closeAccount(String accountNumber) throws AccountClosingException {
        if(accounts.size() == 1)
            throw new AccountClosingException("Cannot close the last account.");
        for(var account : accounts)
            if(account.getAccountNumber().equals(accountNumber))
                if(account.getTotalAmount() != 0)
                    throw new AccountClosingException("Cannot close an account that still has a balance");
        for(int i = 0; i < accounts.size(); i++)
            if(accounts.get(i).getAccountNumber().equals(accountNumber)) {
                accounts.remove(i);
                break;
            }
    }

    @Override
    public String toString() {
        return "\n\tClient [name=" + name + ", address=" + address + ", dob=" + dateOfBirth + ", accounts=" + accounts + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
