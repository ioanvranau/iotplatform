package com.platform.iot.api.message.server;

import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class MigrationMessage extends ServerMessage{
    public static final String FIELD_SERVER_ADDRESS = "serverAddress";
    public static final String FIELD_SERVER_PORT = "serverPort";
    public static final String FIELD_TOKEN = "token";

    private String token;
    private String serverAddress;
    private int serverPort;

    public static MigrationMessage create(User user) {
        MigrationMessage migrationMessage = new MigrationMessage();
        migrationMessage.setToken(user.getToken());
        migrationMessage.setServerAddress(user.getMigration().getServer().getAddress());
        migrationMessage.setServerPort(user.getMigration().getServer().getPort());

        return migrationMessage;
    }

    public MigrationMessage() {
        this.setType(MESSAGE.MIGRATION);
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_TOKEN, getToken());
        jsonObject.put(FIELD_SERVER_ADDRESS, getServerAddress());
        jsonObject.put(FIELD_SERVER_PORT, getServerPort());
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "MigrationMessage{" +
                "token='" + token + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}
