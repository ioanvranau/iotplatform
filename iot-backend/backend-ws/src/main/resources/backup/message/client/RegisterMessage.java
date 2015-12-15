package backup.message.client;

import backup.message.Message;
import net.sf.json.JSONObject;

public class RegisterMessage extends Message {
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_EMAIL = "email";

    private String username;
    private String password;
    private String email;

    public RegisterMessage(JSONObject jsonObject) {
        try {
            create(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(JSONObject object) throws Exception {
        this.setType(ClientMessage.MESSAGE.REGISTER);
        username = object.optString(FIELD_USERNAME);
        password = object.optString(FIELD_PASSWORD);
        email = object.optString(FIELD_EMAIL);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
