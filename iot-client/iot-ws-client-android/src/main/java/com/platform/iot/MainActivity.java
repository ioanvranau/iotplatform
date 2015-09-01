package com.platform.iot;

/**
 * Created by vranau on 8/30/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private String username = "Default Username";

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
    }

    public void loginUser(View view) {
        this.username = ((TextView) findViewById(R.id.editTextUsername)).getText().toString();
        Intent i = new Intent(MainActivity.this, HomeUserActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}