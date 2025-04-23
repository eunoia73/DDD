package com.example.ddd.domain.order;

public class Order {
    private OrderState state;
    private ShippingInfo shippingInfo;

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        if (!state.isShippingChangeable()) {
            throw new IllegalArgumentException("배송지 변경 불가" + state);
        }
        this.shippingInfo = newShippingInfo;
    }

}
