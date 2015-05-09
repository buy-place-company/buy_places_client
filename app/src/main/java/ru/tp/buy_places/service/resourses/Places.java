package ru.tp.buy_places.service.resourses;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.content_provider.BuyPlacesContract;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Places implements Resource {
    private List<Place> mPlaces = new ArrayList<>();


    public Places(JSONArray placesJSONObject) {
        if (placesJSONObject != null) {
            for (int i = 0; i < placesJSONObject.length(); i++) {
                Place object = new Place(placesJSONObject.optJSONObject(i));
                mPlaces.add(object);
            }
        }
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }


    public void writeToDatabaseAsPlacesAroundThePoint(Context context) {
        ContentValues aroundThePointIsFalseContentValues = new ContentValues();
        aroundThePointIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, false);
        int deleted = context.getContentResolver().delete(BuyPlacesContract.Places.CONTENT_URI, BuyPlacesContract.Places.ONLY_AROUND_THE_POINT_SELECTION, null);
        int updated = context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePointIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_POINT_SELECTION, null);
        Log.d("AroundThePoint", "deleted: " + deleted + "/ updated: " + updated + "\n");
        for (Place place : mPlaces) {
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
            values.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, true);
            context.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
        }
    }

    public void writeToDatabaseAsPlacesAroundThePlayer(Context context) {
        ContentValues aroundThePlayerIsFalseContentValues = new ContentValues();
        aroundThePlayerIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, false);
        int updated = context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePlayerIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_PLAYER_SELECTION, null);
        Log.d("AroundThePlayer", "updated :" + updated + "\n");
        for (Place place : mPlaces) {
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
            values.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, true);
            values.put(BuyPlacesContract.Places.COLUMN_IS_VISITED_IN_THE_PAST, true);
            context.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
        }
    }
}