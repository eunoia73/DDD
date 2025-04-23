package com.example.ddd.domain.order;

public class ShippingInfo {
    private Receiver receiver;
    private Address address;

    public Receiver getReceiver() {
        return receiver;
    }

    public Address getAddress() {
        return address;
    }
}
