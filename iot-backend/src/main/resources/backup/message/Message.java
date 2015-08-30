package backup.message;

public class Message {

    public static final String FIELD_TYPE = "type";

    private String type;

    public Message(String type) {
        this.type = type;
    }

    public Message() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
