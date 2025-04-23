package com.example.ddd.domain.order;

public class ShippingInfo {
    private String receiverName;
    private String receiverPhoneNumber;
    private String ShippingAddress1;
    private String ShippingAddress2;
    private String shippingZipCode;

    public ShippingInfo(String receiverName, String receiverPhoneNumber, String shippingAddress1, String shippingAddress2, String shippingZipCode) {
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        ShippingAddress1 = shippingAddress1;
        ShippingAddress2 = shippingAddress2;
        this.shippingZipCode = shippingZipCode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public String getShippingAddress1() {
        return ShippingAddress1;
    }

    public String getShippingAddress2() {
        return ShippingAddress2;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }

}
