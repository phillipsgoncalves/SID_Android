package com.example.sid.sid_android.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        /** escrever no relatorio **/
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        ip.setText(sharedPref.getString("IP", ""));
        port.setText(sharedPref.getString("PORT", ""));

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new UserLogin(ip.getText().toString(), port.getText().toString());

        /** escrever no relatorio **/
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IP", String.valueOf(ip.getText()));
        editor.putString("PORT", String.valueOf(port.getText()));
        editor.apply();



        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

