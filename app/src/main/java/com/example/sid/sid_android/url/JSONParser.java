package com.example.sid.sid_android.url;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {

    public JSONArray getJSONFromUrl(final String url, HashMap<String, String> params) {

        JSONArray jObj = null;
        try {
            StringBuilder sb_params = new StringBuilder();
            Log.d(this.getClass().getName(), "sb_params: " + sb_params);
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
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            //conn.connect();   // isto pode resolver - tenho de testar, o openConnection supostamente ja trata isto.
            String paramsString = sb_params.toString();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
            int responseCode = conn.getResponseCode();
            Log.d(this.getClass().getName(), "URL: " + url);
            Log.d(this.getClass().getName(), "sb_params: " + sb_params);
            Log.d(this.getClass().getName(), "response_code : " + responseCode);


            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

//            InputStream in = new BufferedInputStream(conn.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            StringBuilder result = new StringBuilder();
//            String line;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();




//            HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost(url);
//
//            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//            post.setEntity(new UrlEncodedFormEntity(urlParameters));
//
//            HttpResponse response = client.execute(post);
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + post.getEntity());
//            System.out.println("Response Code : " +
//                    response.getStatusLine().getStatusCode());
//
//
//            BufferedReader rd = new BufferedReader(
//                    new InputStreamReader(response.getEntity().getContent()));
//
//            StringBuffer result = new StringBuffer();
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }


            Log.d(this.getClass().getName(), "toString " + response.toString());
            JSONObject JSONobj = new JSONObject(response.toString());
            jObj = JSONobj.getJSONArray("posts");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }
}

