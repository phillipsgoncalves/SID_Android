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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Login extends Activity implements View.OnClickListener {
    private EditText ip, port,passToPass,mailToPass;
    private Button login, signIn;
    private ViewFlipper flipper;
    private ProgressDialog pDialog;

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

        mailToPass = (EditText) findViewById(R.id.email);
        passToPass = (EditText) findViewById(R.id.password);
        mailToPass.setText(sharedPref.getString("email",""));
        passToPass.setText(sharedPref.getString("password",""));

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
            editor.putString("email", String.valueOf(mailToPass.getText()));
            editor.putString("password", String.valueOf(passToPass.getText()));
            editor.apply();

            if(mailToPass.getText() != null && passToPass.getText() != null) {
                new LoginValidation(this, String.valueOf(ip.getText()), String.valueOf(port.getText()), String.valueOf(mailToPass.getText()), String.valueOf(passToPass.getText())).execute();
            } else {
                Toast.makeText(Login.this, "You can't leave these fields empty", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Context getC() {
        return this;
    }


    private class LoginValidation extends AsyncTask<String, String, String> {

        private Activity activity;
        private boolean loginSuccess;
        private String ip;
        private String port;
        private String email;
        private String password;

        public LoginValidation(Activity activity, String ip, String port, String email, String password) {
            this.activity = activity;
            this.ip = ip;
            this.port = port;
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loggin in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> jsonParams = new HashMap<String, String>();
            jsonParams.put("user", email);
            jsonParams.put("password", password);
            jsonParams.put("format", "json");

            JSONParser jsonParser = new JSONParser();
            JSONArray json = jsonParser.getJSONFromUrl("http://" + ip + ":" + port + "/checkLogin.php", jsonParams);
            try {
                List<String> list = new LinkedList<String>();
                for (int i = 0; i < json.length(); i++) {
                    JSONObject a = null;
                    a = json.getJSONObject(i);
                    JSONObject c = a.getJSONObject("post");
                    // apanhar aqui a resposta


                    loginSuccess = true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();

            if(loginSuccess) {
                Intent i = new Intent(activity, MainActivity.class);
                startActivity(i);
                finish();
            }else
                Toast.makeText(activity, "Wrong login, please try again.", Toast.LENGTH_LONG).show();
        }
    }
}

