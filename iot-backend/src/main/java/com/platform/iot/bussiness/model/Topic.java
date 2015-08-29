package com.platform.iot.bussiness.model;

/**
 * Created by Magda on 17.05.2014.
 *
 */
public class Topic {
    public static final String FIELD_CODE = "code";
    public static final String FIELD_PRICE = "price";

    private String code;
    private double price;

    public Topic(String code, double price) {
        this.code = code;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "[" +
                "code='" + code + '\'' +
                ", price=" + price +
                ']';
    }
}
