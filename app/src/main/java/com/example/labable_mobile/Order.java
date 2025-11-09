package com.example.labable_mobile;
import com.example.labable_mobile.OrderItem;
public class Order {
    public static int orderCount;
    private String orderId;
    private String address;
    private OrderItem orderItems[];
    private String serviceType;
    private String transferMode;
    private String transferDate;
    private String claimMode;
    private String paymentMethod;
    private String notes;
    private double totalPrice;

    public String getAddress() {
        return address;
    }

    public OrderItem[] getOrderItems() {
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

    public String getClaimMode() {
        return claimMode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public Order(String address, OrderItem[] orderItems, String serviceType, String transferMode, String transferDate, String claimMode, String paymentMethod, String notes) {
        this.address = address;
        this.orderItems = orderItems;
        this.serviceType = serviceType;
        this.transferMode = transferMode;
        this.transferDate = transferDate;
        this.claimMode = claimMode;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.orderId = "ORD-"+String.format(String.valueOf(++this.orderCount));
    }

    private double calculateTotal(){
        //TODO: calculate
        return 0;
    }

}
