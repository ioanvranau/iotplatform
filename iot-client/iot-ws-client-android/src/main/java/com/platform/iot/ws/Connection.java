package com.platform.iot.ws;

import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.iot.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by vranau on 9/3/2015.
 */
public class Connection {

    private static WebSocketClient mWebSocketClient = null;

    public static WebSocketClient startConnection(String host) {
        return connectWebSocket(host);
    }

    public static String token = null;

    private static WebSocketClient connectWebSocket(String host) {
        if (mWebSocketClient != null && host == null) {
            return mWebSocketClient;
        }
        URI uri;
        try {
            String spec = "ws://" + host + ":8086/prices";
            Log.i("URL", spec);
            uri = new URI(spec);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                token = null;
//                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.i("WS message ", s);

                String type = readJsonValue(message, "type");

                if (type.equals("token")) {
                    token = readJsonValue(message, "token");
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
        return mWebSocketClient;
    }

    public static void main(String[] args) {
        String json = "{\"type\":\"token\",\"token\":\"b5ca337e9dd8bc00635bd5ce73ac74e2\",\"id\":1,\"email\":\"johnsmith@gmail.com\",\"username\":\"a\"}";
        readJsonValue(json, "type");
    }

    public static String readJsonValue(String json, String propName) {
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

//read JSON like DOM Parser
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(new StringReader(json));
            JsonNode idNode = rootNode.path(propName);
            String s = idNode.asText();
            System.out.println("id = "+ s);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }
}
