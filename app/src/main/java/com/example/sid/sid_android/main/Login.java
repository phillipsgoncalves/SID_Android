package com.example.sid.sid_android.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sid.sid_android.R;
import com.example.sid.sid_android.util.UserLogin;

public class Login extends Activity implements View.OnClickListener {
    private EditText ip, port;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iplogin);

        ip = (EditText) findViewById(R.id.ip);

        port = (EditText) findViewById(R.id.port);


        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new UserLogin(ip.getText().toString(), port.getText().toString());
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

