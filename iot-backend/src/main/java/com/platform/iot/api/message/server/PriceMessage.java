package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class PriceMessage extends ServerMessage {

    private static final String FIELD_CODE = "code";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_CHANGED = "changed";
    private static final String FIELD_PRODUCER_ID = "producerId";
    public static final String FIELD_NAME = "name";
    private String code;
    private String name;
    private double price;
    private double changed;
    private long producerId;

    public PriceMessage() {
        this.setType(ServerMessage.MESSAGE.PRICE);
    }

    public PriceMessage(String code, double price, double changed, long providerId) {
        this.setType(ServerMessage.MESSAGE.PRICE);
        this.setCode(code);
        this.setPrice(price);
        this.setChanged(changed);
        this.setProducerId(providerId);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getChanged() {
        return changed;
    }

    public void setChanged(double changed) {
        this.changed = changed;
    }

    @Override
    public String toString() {
        return "PriceMessage{" +
                "code='" + code + '\'' +
                ", price=" + price +
                ", changed=" + changed +
                ", producerId=" + producerId +
                '}';
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_CODE, getCode());
        jsonObject.put(FIELD_PRICE, getPrice());
        jsonObject.put(FIELD_CHANGED, getChanged());
        jsonObject.put(FIELD_PRODUCER_ID, getProducerId());
        jsonObject.put(FIELD_NAME, getName());
        return jsonObject.toString();
    }
}
