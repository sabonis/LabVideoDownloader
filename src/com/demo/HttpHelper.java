package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sabonis on 5/25/2014.
 */
public class HttpHelper {
    public static String get(String url) {
        StringBuffer response = new StringBuffer();
        try {
            HttpURLConnection con = getHttpURLConnection(url);
            String inputLine;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static String get(String url, Filter filter) {
        StringBuffer response = new StringBuffer();
        try {
            HttpURLConnection con = getHttpURLConnection(url);
            String inputLine;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                if (filter.filter(inputLine)) {
                    response.append(inputLine);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.getResponseCode();
        return con;
    }

    interface Filter {
        boolean filter(String inputLine);
    }
}
