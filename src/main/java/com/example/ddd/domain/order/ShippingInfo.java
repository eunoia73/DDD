package com.example.ddd.domain.order;

public class ShippingInfo {
    private String address;
    private String receiver;

    public ShippingInfo(String address, String receiver) {
        this.address = address;
        this.receiver = receiver;
    }
}
