package backup.message.server;


import net.sf.json.JSONObject;

public class PriceMessage extends ServerMessage {

    private static final String FIELD_CODE = "code";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_CHANGED = "changed";
    private String code;
    private double price;
    private double changed;

    public PriceMessage() {
        this.setType(ServerMessage.MESSAGE.PRICE);
    }

    public PriceMessage(String code, double price, double changed) {
        this.setType(ServerMessage.MESSAGE.PRICE);
        this.setCode(code);
        this.setPrice(price);
        this.setChanged(changed);
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
                '}';
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_CODE, getCode());
        jsonObject.put(FIELD_PRICE, getPrice());
        jsonObject.put(FIELD_CHANGED, getChanged());
        return jsonObject.toString();
    }
}
