package controllers;

import main.JServer;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;

public class HTTPController {

    public static int post(String url, JSONObject json) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            return con.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
