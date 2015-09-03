package com.platform.iot;

/**
 * Created by vranau on 8/30/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.platform.iot.ws.Connection;

import org.java_websocket.client.WebSocketClient;

public class MainActivity extends Activity {

    private static final String HOST = "10.0.2.2";
    private String username = "Default Username";
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Hello world!");
        Connection.startConnection(HOST);
    }

    public void loginUser(View view) {
        this.username = ((TextView) findViewById(R.id.editTextUsername)).getText().toString();
        this.password= ((TextView) findViewById(R.id.editTextPassword)).getText().toString();
        Intent i = new Intent(MainActivity.this, HomeUserActivity.class);
        i.putExtra("username", username);
        i.putExtra("password", password);

        WebSocketClient b = Connection.startConnection(null);
        if(b != null) {
            String loginM = "{type:'login', username:'"+username+"', password:'"+password+"'}";
            b.send(loginM);
            while(Connection.token == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startActivity(i);
        }
    }
}