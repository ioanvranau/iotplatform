package com.platform.iot.api.bussiness.model;

import javax.persistence.*;

/**
 * Created by Magda on 17.05.2014.
 *
 */
@Entity
@javax.persistence.Table(name = "topics")
public class Topic {
    public static final String FIELD_CODE = "code";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DEFAULT_SUBSCRIBED = "defaultSubscribed";

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "producerId")
    private long producerId;

    @Column(name = "defaultSubscribed")
    private boolean defaultSubscribed;

    public Topic() {
    }

    public Topic(String code, String name, double price, boolean defaultSubscribed, long producerId) {
        this.code = code;
        this.price = price;
        this.name = name;
        this.defaultSubscribed = defaultSubscribed;
        this.producerId = producerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefaultSubscribed() {
        return defaultSubscribed;
    }

    public void setDefaultSubscribed(boolean defaultSubscribed) {
        this.defaultSubscribed = defaultSubscribed;
    }



    @Override
    public String toString() {
        return "[" +
                "code='" + code + '\'' +
                ", price=" + price +
                ", producerId=" + producerId +
                ']';
    }
}
