package com.example.sid.sid_android.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.sid.sid_android.R;
import com.example.sid.sid_android.url.JSONParser;
import com.example.sid.sid_android.util.Advertisement;
import com.example.sid.sid_android.util.UserLogin;

import java.util.HashMap;
import java.util.List;

public class Login extends Activity implements View.OnClickListener {
    private EditText ip, port;
    private Button login, signIn;
    private ViewFlipper flipper;
    private String mailToPass = "";
    private String passToPass = "";

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

        signIn = (Button) findViewById(R.id.SignIn);
        signIn.setOnClickListener(this);

        flipper= (ViewFlipper) findViewById(R.id.flipper);
        flipper.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //Log.d("cliquei no botoa",""+v.getId());

        new UserLogin(ip.getText().toString(), port.getText().toString());

        /** escrever no relatorio **/
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        if(v.getId() == R.id.login){
            editor.putString("IP", String.valueOf(ip.getText()));
            editor.putString("PORT", String.valueOf(port.getText()));
            editor.apply();
            flipper.showNext();

        } else if (v.getId()== R.id.SignIn){
            //getEmailToPass();
            editor.putString("email", mailToPass);
            editor.putString("password", passToPass);
            editor.apply();
          //  new ResetDB().execute();

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public Context getC() {
        return this;
    }



}

