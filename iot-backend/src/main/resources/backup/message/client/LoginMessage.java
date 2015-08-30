package backup.message.client;

/**
 * Created by Magda Gherasim
 */
public class LoginMessage extends ClientMessage{
    public final static String FIELD_EMAIL = "email";
    public final static String FIELD_PASSWORD = "password";

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "email='" + email + '\'' +
                ", password='" + "********" + '\'' +
                '}';
    }
}
