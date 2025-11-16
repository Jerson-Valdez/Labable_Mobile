package com.example.labable_mobile;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class AccountManager implements Serializable {
    public HashMap<String, Account> accounts = new HashMap<>();
    public Account loggedInAccount = null;
    
    public AccountManager() {
        
    }

    public Account getLoggedInAccount() {
        return loggedInAccount;
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<String, Account> account) {
        this.accounts = account;
    }

    public String generateId() {
        return "ACC-" + accounts.size() + 1;
    }

    public Account getAccountById(String id) {
        for (Account account: accounts.values()) {
            if (account.getEmail().equals(id)) {
                return account;
            }
        }

        return null;
    }

    public Account getAccountByEmailAndPassword(String email, String password) throws Error {
        for (Account account: accounts.values()) {
            if (account.getEmail().equals(email) && account.getPassword().equals(password)) {
                return account;
            } else {
                throw new Error("credentials");
            }
        }
        return null;
    }

    public boolean emailExists(String email) {
        for (Account account: accounts.values()) {
            if (account.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public void updateAccountOrder(Order order) {
        for (Order accountOrder: loggedInAccount.getOrders().values()) {
            Log.d("Order", accountOrder.getOrderId());
            Log.d("Order 2", order.getOrderId());
            if (order.getOrderId().equals(accountOrder.getOrderId())) {
                HashMap<String, Order> orders = loggedInAccount.getOrders();
                orders.replace(order.getOrderId(), order);
                loggedInAccount.setOrders(orders);
                accounts.replace(loggedInAccount.getId(), loggedInAccount);
                return;
            }
        }
    }

    public void deleteAccount(Account account) {
        accounts.remove(account.getId());
    }

    public void updateAccount(Account account) {
        accounts.replace(account.getId(), account);
    }

    public void login(Account account) {
        loggedInAccount = account;
    }

    public void logout() {
        loggedInAccount = null;
    }
}
