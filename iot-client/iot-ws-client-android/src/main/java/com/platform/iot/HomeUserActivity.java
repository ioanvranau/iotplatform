package com.platform.iot;

/**
 * Created by vranau on 8/30/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        ((TextView) findViewById(R.id.filledUsername)).setText(username);
    }
}