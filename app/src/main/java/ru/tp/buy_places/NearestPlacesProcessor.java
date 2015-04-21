package ru.tp.buy_places;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static ru.tp.buy_places.content_provider.BuyPlacesContract.Places;

/**
 * Created by Ivan on 19.04.2015.
 */
public class NearestPlacesProcessor {
    private Context mContext;

    public NearestPlacesProcessor(Context context) {
        mContext = context;
    }


    public void getNearestPlaces(NearestPlacesProcessorCallback callback) {
        String URL_ROOT = "http://private-f5e37f-testapi901.apiary-mock.com";
        JSONObject response = null;
        try {
            URL url = new URL(URL_ROOT + "/objects_near");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String responseString = "";
            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine())
                responseString += scanner.nextLine();
            response = new JSONObject(responseString);
            httpURLConnection.disconnect();
        } catch (JSONException | IOException e) {
            response = null;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NearestPlaces nearestPlaces = new NearestPlaces(response.optJSONArray("data"));
        updateContentProvider(nearestPlaces);
        callback.send(response.optInt("status"));
    }

    private void updateContentProvider(NearestPlaces result) {
        List<Place> places = result.getPlaces();
        for (Place place : places) {
            String id = place.getId();
            long checkinsCount = place.getCheckinsCount();
            long usersCount = place.getUsersCount();
            long tipCount = place.getTipCount();
            String name = place.getName();
            String category = place.getCategory();
            String type = place.getType();
            String level = place.getLevel();
            long owner = place.getOwner();
            long price = place.getPrice();
            float latitude = place.getLatitude();
            float longitude = place.getLongitude();

            ContentValues values = new ContentValues();
            values.put(Places.COLUMN_ID, id);
            values.put(Places.COLUMN_CHECKINS_COUNT, checkinsCount);
            values.put(Places.COLUMN_USERS_COUNT, usersCount);
            values.put(Places.COLUMN_TIP_COUNT, tipCount);
            values.put(Places.COLUMN_NAME, name);
            values.put(Places.COLUMN_CATEGORY, category);
            values.put(Places.COLUMN_TYPE, type);
            values.put(Places.COLUMN_LEVEL, level);
            values.put(Places.COLUMN_OWNER, owner);
            values.put(Places.COLUMN_PRICE, price);
            values.put(Places.COLUMN_LATITUDE, latitude);
            values.put(Places.COLUMN_LONGITUDE, longitude);
            mContext.getContentResolver().insert(Places.CONTENT_URI, values);
        }
    }
}
