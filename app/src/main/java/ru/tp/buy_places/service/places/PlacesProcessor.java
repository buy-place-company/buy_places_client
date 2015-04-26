package ru.tp.buy_places.service.places;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Places;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 19.04.2015.
 */
public class PlacesProcessor extends Processor {

    private final double mLatitude;
    private final double mLongitude;

    PlacesProcessor(Context context, OnProcessorResultListener listener, double latitude, double longitude) {
        super(context, listener);
        mLatitude = latitude;
        mLongitude = longitude;

    }


    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("code");
        String message = responseJSONObject.optString("message", null);
        JSONArray dataJSONArray = responseJSONObject.optJSONArray("data");
        Places places = new Places(dataJSONArray);
        return new Response(status, message, places);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", Double.toString(mLatitude));
        params.put("lng", Double.toString(mLongitude));
        return new Request(mContext, "/objects_near", Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response result) {
        Places places = (Places) result.getData();
        List<Place> placeList = places.getPlaces();
        for (Place place : placeList) {
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
            values.put(BuyPlacesContract.Places.COLUMN_ID, id);
            values.put(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT, checkinsCount);
            values.put(BuyPlacesContract.Places.COLUMN_USERS_COUNT, usersCount);
            values.put(BuyPlacesContract.Places.COLUMN_TIP_COUNT, tipCount);
            values.put(BuyPlacesContract.Places.COLUMN_NAME, name);
            values.put(BuyPlacesContract.Places.COLUMN_CATEGORY, category);
            values.put(BuyPlacesContract.Places.COLUMN_TYPE, type);
            values.put(BuyPlacesContract.Places.COLUMN_LEVEL, level);
            values.put(BuyPlacesContract.Places.COLUMN_OWNER, owner);
            values.put(BuyPlacesContract.Places.COLUMN_PRICE, price);
            values.put(BuyPlacesContract.Places.COLUMN_LATITUDE, latitude);
            values.put(BuyPlacesContract.Places.COLUMN_LONGITUDE, longitude);
            mContext.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
        }
    }
}
