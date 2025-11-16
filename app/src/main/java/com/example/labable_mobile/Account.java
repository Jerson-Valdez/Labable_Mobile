package com.example.labable_mobile;

import java.io.Serializable;
import java.util.HashMap;

public class Account implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private HashMap<String, Order> orders;
    private boolean remember;

    public Account(String id, HashMap<String, Order> orders, String address, String phone, String password, String email, String name) {
        this.id = id;
        this.orders = orders;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashMap<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, Order> orders) {
        this.orders = orders;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
