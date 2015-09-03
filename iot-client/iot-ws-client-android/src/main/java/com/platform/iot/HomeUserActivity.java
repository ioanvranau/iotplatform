package com.platform.iot;

/**
 * Created by vranau on 8/30/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.platform.iot.ws.Connection;

public class HomeUserActivity extends Activity {

    private String username = "Default Username1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);


    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView) findViewById(R.id.filledUsername)).setText(username);
//        TextView textView = (TextView) findViewById(R.id.text_view);
//        textView.setText("Hello world!");
    }

    public void fillUsername(View view) {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        TextView deviceDataTextView = (TextView) findViewById(R.id.filledUsername);

        TextView tokenTextView = (TextView) findViewById(R.id.textViewToken);
        tokenTextView.setText(Connection.token);
        String deviceData = deviceDataTextView.getText().toString();

        String jsonData = "{type:'addDevice', token:'" + Connection.token + "', param1:'"+deviceData+"', param2:'param2', param3:'param3', param4:'param4', param5:'param5'}";
        Log.i("jsonData", jsonData);
        Connection.startConnection(null).send(jsonData);
//        deviceDataTextView.setText("");
    }
}