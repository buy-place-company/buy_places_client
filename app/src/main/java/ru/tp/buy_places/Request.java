package ru.tp.buy_places;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Ivan on 22.04.2015.
 */
public class Request {

    public final Context mContext;
    private final String mUrlRoot;
    private final String mPath;
    private final RequestMethod mRequestMethod;
    private final Map<String, String> mParams;

    public Request(Context context, String path, RequestMethod requestMethod, Map<String, String> params) {
        mContext = context;
        mUrlRoot = context.getString(R.string.url_root);
        mPath = path;
        mRequestMethod = requestMethod;
        mParams = params;
    }

    public JSONObject execute() {
        JSONObject responseJSONObject;
        switch (mRequestMethod) {
            case GET:
                responseJSONObject =  doGet(mPath, mParams);
                break;
            case POST:
                responseJSONObject = doPost(mPath, mParams);
                break;
            default:
                responseJSONObject = null;
        }
        return responseJSONObject;
    }


    private JSONObject doGet(String path, Map<String, String> params) {
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        StringBuilder paramsQuery = new StringBuilder();
        JSONObject responseJSONObject;
        try {
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                paramsQuery.append(entry.getKey()).append("=").append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                if (iterator.hasNext()) {
                    paramsQuery.append("&");
                }
            }
            URL url = new URL(mUrlRoot + path + "?" + paramsQuery.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String responseString = "";
            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine())
                responseString += scanner.nextLine();
            responseJSONObject = new JSONObject(responseString);
            httpURLConnection.disconnect();

        } catch (JSONException | IOException e) {
            responseJSONObject = null;
            e.printStackTrace();
        }
        return responseJSONObject;
    }

    private JSONObject doPost(String path, Map<String, String> formData) {
        Iterator<Map.Entry<String, String>> iterator = formData.entrySet().iterator();
        StringBuilder paramsQuery = new StringBuilder();
        JSONObject response;
        try {
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                paramsQuery.append(entry.getKey()).append("=").append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                if (iterator.hasNext()) {
                    paramsQuery.append("&");
                }
            }
            URL url = new URL(mUrlRoot + path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setFixedLengthStreamingMode(paramsQuery.toString().getBytes().length);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            printWriter.write(paramsQuery.toString());
            printWriter.close();

            String responseString = "";
            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine())
                responseString += scanner.nextLine();
            response = new JSONObject(responseString);
            httpURLConnection.disconnect();
        } catch (JSONException | IOException e) {
            response = null;
            e.printStackTrace();
        }
        return response;
    }


    enum RequestMethod {
        GET,
        POST
    }
}
