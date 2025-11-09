package com.example.labable_mobile;

public class WashableItem {
    private String name;
    private double itemsPerKg; // The data you wanted to store

    // Constructor
    public WashableItem(String name, double itemsPerKg) {
        this.name = name;
        this.itemsPerKg = itemsPerKg;
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public double getItemsPerKg() {
        return itemsPerKg;
    }

    @Override
    public String toString() {
        return this.name; // We only want to show the name
    }
}
