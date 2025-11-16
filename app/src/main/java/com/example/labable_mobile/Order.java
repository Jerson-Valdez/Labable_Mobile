package com.example.labable_mobile;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private String orderId;
    private String status;
    private String address;
    private ArrayList<OrderItem> orderItems;
    private String serviceType;
    private String transferMode;
    private String transferDate;
    private String transferTime;
    private String claimMode;
    private String paymentMethod;
    private String notes;
    private double totalPrice;

    public String getAddress() {
        return address;
    }

    public String getStatus() { return status; };

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getTransferMode() {
        return transferMode;
    }

    public String getTransferDate() {
        return transferDate;
    }
    public String getTransferTime() {
        return transferTime;
    }

    public String getClaimMode() {
        return claimMode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(String id, String address, ArrayList<OrderItem> orderItems, String serviceType, String transferMode, String transferDate, String transferTime, String claimMode, String paymentMethod, String notes, double total) {
        this.status = "Pending";
        this.address = address;
        this.orderItems = orderItems;
        this.serviceType = serviceType;
        this.transferMode = transferMode;
        this.transferDate = transferDate;
        this.transferTime = transferTime;
        this.claimMode = claimMode;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.orderId = id;
        this.totalPrice = total;
    }

}
