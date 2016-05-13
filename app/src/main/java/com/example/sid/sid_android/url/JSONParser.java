package com.example.sid.sid_android.url;

import android.net.Uri;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class JSONParser {

    public JSONArray getJSONFromUrl(final String url, HashMap<String, String> params) {
        JSONArray jObj = null;
        try {
            StringBuilder sb_params = new StringBuilder();
            int i = 0;
            for (String key : params.keySet()) {
                if (i != 0) {
                    sb_params.append("&");
                }
                sb_params.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
                i++;
            }
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.connect();   // isto pode resolver - tenho de testar, o openConnection supostamente ja trata isto.
            String paramsString = sb_params.toString();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
            jObj = new JSONArray(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }
}

