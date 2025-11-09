package com.example.labable_mobile;

import java.io.Serializable;

// Implement Serializable so it can be passed in an Intent
public class OrderItem implements Serializable {
    private String name;
    private int quantity;

    // Constructor
    public OrderItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}