package ru.tp.buy_places.service.network;

import android.content.Context;
import android.util.Log;

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

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 22.04.2015.
 */
public class Request {

    private static final String LOG_TAG = Request.class.getSimpleName();

    private final Context mContext;
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
        JSONObject unknownErrorJSONObject = null;
        try {
            unknownErrorJSONObject = new JSONObject("{status: 0}");
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                paramsQuery.append(entry.getKey()).append("=").append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                if (iterator.hasNext()) {
                    paramsQuery.append("&");
                }
            }
            
            URL url = new URL(mUrlRoot + path + "?" + paramsQuery.toString());
            Log.i(LOG_TAG, "REQUEST: GET " + url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String responseString = "";
            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine())
                responseString += scanner.nextLine();
            Log.i(LOG_TAG, "RESPONSE: " + responseString);
            responseJSONObject = new JSONObject(responseString);
            httpURLConnection.disconnect();

        } catch (JSONException | IOException e) {
            responseJSONObject = unknownErrorJSONObject;
            e.printStackTrace();
        }
        return responseJSONObject;
    }

    private JSONObject doPost(String path, Map<String, String> formData) {
        Iterator<Map.Entry<String, String>> iterator = formData.entrySet().iterator();
        StringBuilder paramsQuery = new StringBuilder();
        JSONObject response;
        JSONObject unknownErrorJSONObject = null;
        try {
            unknownErrorJSONObject = new JSONObject("{status: 0}");
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                paramsQuery.append(entry.getKey()).append("=").append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                if (iterator.hasNext()) {
                    paramsQuery.append("&");
                }
            }
            URL url = new URL(mUrlRoot + path);
            Log.i(LOG_TAG, "REQUEST: POST " + url.toString());
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
            Log.i(LOG_TAG, "RESPONSE: " + responseString);
            response = new JSONObject(responseString);
            httpURLConnection.disconnect();
        } catch (JSONException | IOException e) {
            response = unknownErrorJSONObject;
            e.printStackTrace();
        }
        return response;
    }


    public enum RequestMethod {
        GET,
        POST
    }
}
