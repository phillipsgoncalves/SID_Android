package com.example.sid.sid_android.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sid.sid_android.R;
import com.example.sid.sid_android.database.DatabaseHandler;
import com.example.sid.sid_android.url.JSONParser;
import com.example.sid.sid_android.util.Advertisement;
import com.example.sid.sid_android.util.Company;
import com.example.sid.sid_android.util.UserLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private DatabaseHandler handler;
    private ProgressDialog pDialog;
    private ListView listView;
    private Button button, button2;
    private String mailToPass = "";
    private String passToPass = "";

    private static final String IP = UserLogin.getInstance().getIp();
    private static final String PORT = UserLogin.getInstance().getPort();

    private static final String READ_ADS = "http://" + IP + ":" + PORT + "/getAds.php";
    private static final String READ_COMP = "http://" + IP + ":" + PORT + "/getComps.php";
    private static final String SYNCHRONIZE = "http://" + IP + ":" + PORT + "/synchronize.php";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.main);
        handler = new DatabaseHandler(this);
        handler.open();
        listView = (ListView) findViewById(R.id.adList);
        button = (Button) findViewById(R.id.btn_New);
        button2 = (Button) findViewById(R.id.btn_Old);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        if (handler.getAllAds().size() > 0) {
            List<Advertisement> ads = handler.getAllAds();
            ArrayAdapter<Advertisement> adapter = new InteractiveArrayAdapter((Activity) getC(), ads, handler);
            listView.setAdapter(adapter);
        }


    }

    public Context getC() {
        return this;
    }

    public void addToList(JSONParser jParser, HashMap<String, String> params, String emails, String passs) {
        try {

            params.put("email", emails);
            params.put("pass", passs);
            JSONArray json = jParser.getJSONFromUrl(READ_ADS, params);
            List<String> list = new LinkedList<String>();
            handler.clearAds();
            for (int i = 0; i < json.length(); i++) {
                JSONObject c = json.getJSONObject(i);
                int numero_anuncio = c.getInt("Numero_Anuncio");
                String lingua_origem = c.getString("Lingua_Origem");
                String designacao_software = c.getString("Designacao_Software");
                String lingua_destino = c.getString("Lingua_Destino");
                String data_inicio_trabalho = c.getString("Data_Inicio_Trabalho");
                int numero_dias = c.getInt("Numero_Dias");
                int numero_palavras = c.getInt("Numero_Palavras");
                double valor = c.getDouble("Valor");
                String estado = c.getString("Estado");
                String email = c.getString("Email");
                list.add(email);
                handler.insertAd(new Advertisement(numero_anuncio, lingua_origem, lingua_destino, numero_palavras,
                        valor, data_inicio_trabalho, numero_dias, designacao_software, estado, email));
            }
            handler.clearComps();
            for (String s : list) {
                params.clear();
                params.put("email", s);
                JSONArray json2 = jParser.getJSONFromUrl(READ_COMP, params);
                for (int i = 0; i < json2.length(); i++) {
                    JSONObject a = json2.getJSONObject(i);
                    String name = a.getString("Nome_Utilizador");
                    String email = a.getString("Email");
                    String password = a.getString("Senha");
                    String apresentacao = a.getString("Apresentacao");
                    handler.insertCompany(new Company(name, email, password, apresentacao));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getEmailToPass() {
        mailToPass = "";
        passToPass = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(getC());
        builder.setTitle("Anuncios Utilizador");

        LinearLayout layout = new LinearLayout(getC());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText emailBox = new EditText(getC());
        emailBox.setHint("Email");
        layout.addView(emailBox);

        final EditText passwordBox = new EditText(getC());
        passwordBox.setHint("Password");
        layout.addView(passwordBox);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mailToPass = emailBox.getText().toString();
                passToPass = passwordBox.getText().toString();
                new ResetDB().execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void synchronizeData(JSONParser jParser, HashMap<String, String> params) {
        JSONObject anuncs = new JSONObject();
        try {
            JSONArray arr = new JSONArray();
            List<Advertisement> ads = handler.getAllAds();
            for(int i = 0; i < ads.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("Numero", ads.get(i).getNumero_anuncio());
                obj.put("Estado", ads.get(i).getEstado());
                arr.put(obj);
            }
            anuncs.put("Anuncios", arr);
        }catch(Exception e) {
            e.printStackTrace();
        }
        params.put("json", anuncs.toString());
        jParser.getJSONFromUrl(SYNCHRONIZE, params);
    }

    @Override
    public void onClick(View v) {
        if (isInternetWorking()) {
            if (v.getId() == R.id.btn_New) {
                getEmailToPass();
            } else if (v.getId() == R.id.btn_Old) {
                new SynchronizeDB().execute();
            }
        } else {
            Toast.makeText(getC(), "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isInternetWorking() {
        ConnectivityManager connectivity = (ConnectivityManager) getC().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    class SynchronizeDB extends AsyncTask<String, String, String> {

        HashMap<String,String> params1;
        JSONParser jParser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Synchronizing Database...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            params1 = new HashMap<String, String>();
            jParser = new JSONParser();
            params1.clear();
            synchronizeData(jParser, params1);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            Toast.makeText(getC(), "Finished Synchronizing", Toast.LENGTH_LONG).show();
        }
    }

    class ResetDB extends AsyncTask<String, String, String> {

        HashMap<String, String> params1;
        JSONParser jParser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Reseting Database...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            params1 = new HashMap<String, String>();
            jParser = new JSONParser();
            params1.clear();
            addToList(jParser, params1, mailToPass, passToPass);
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            List<Advertisement> ads = handler.getAllAds();
            ArrayAdapter<Advertisement> adapter = new InteractiveArrayAdapter((Activity) getC(), ads, handler);
            listView.setAdapter(adapter);
            pDialog.dismiss();
            if (ads.size() == 0) {
                Toast.makeText(getC(), "Either no Entries or User/Pass Incorrect", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getC(), "Reseting Database Complete, Welcome " + mailToPass, Toast.LENGTH_LONG).show();
            }
        }

    }

}

