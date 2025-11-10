package com.example.labable_mobile;

public class ServiceType {
    private String serviceName;
    private double pricePerKilo;

    public String getServiceName() {
        return serviceName;
    }

    public double getPricePerKilo() {
        return pricePerKilo;
    }

    public ServiceType(String serviceName, double pricePerKilo) {
        this.serviceName = serviceName;
        this.pricePerKilo = pricePerKilo;
    }
}
