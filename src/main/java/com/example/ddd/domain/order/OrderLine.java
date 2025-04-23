package com.example.ddd.domain.order;

public class OrderLine {
    private Product product;
    private Money price;
    private int quantity;
    private Money amounts;

    public OrderLine(Product product, Money price, int quantity) {
        this.product = product;
        // price 파라미터가 변경될 때 발생하는 문제 방지 위해 데이터를 복사한 새로운 객체 생성
        this.price = new Money(price.getValue());
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private Money calculateAmounts() {
        return price.multiply(quantity);
    }

    public int getAmounts() {
        return amounts.getValue();
    }

}
