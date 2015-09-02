package com.platform.iot.api.message.server;

import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by Magda Gherasim
 */
public class AllUsersMessage extends ServerMessage {
    private static final String FIELD_USERS = "users";

    private List<User> users;

    public AllUsersMessage(List<User> users) {
        this.setType(MESSAGE.ALL_USERS);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        JSONArray jsonArray = new JSONArray();
        for (User user : this.users){
            JSONObject userObj = new JSONObject();
            userObj.put(User.FIELD_ID, user.getId());
            userObj.put(User.FIELD_TOKEN, user.getToken());
            userObj.put(User.FIELD_USER_TYPE, user.getUsertype());
            userObj.put(User.FIELD_NAME, user.getName());
            userObj.put(User.FIELD_USERNAME, user.getUsername());
            userObj.put(User.FIELD_PASSWORD, user.getPassword());
            userObj.put(User.FIELD_EMAIL, user.getEmail());
            userObj.put(User.FIELD_COUNTRY, user.getCountry());
            jsonArray.add(userObj);
        }
        jsonObject.put(FIELD_USERS, jsonArray);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "AllUsersMessage{" +
                "users=" + users +
                '}';
    }
}
