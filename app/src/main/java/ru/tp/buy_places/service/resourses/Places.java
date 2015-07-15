package ru.tp.buy_places.service.resourses;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.utils.AccountManagerHelper;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Places implements Resource {
    private static final String LOG_TAG = Places.class.getSimpleName();
    private List<Place> mPlaces = new ArrayList<>();

    public Places(){}


    public static Places fromCursor(Cursor data) {
        Places places = new Places();
        while (data.moveToNext()) {
            places.add(Place.fromCursor(data));
        }
        return places;
    }

    public static Places fromJSONArray(JSONArray placesJSONArray) {
        Places places = new Places();
        if (placesJSONArray != null) {
            for (int i = 0; i < placesJSONArray.length(); i++) {
                Place place = Place.fromJSONObject(placesJSONArray.optJSONObject(i));
                places.add(place);
            }
        }
        return places;
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }


    public void writeToDatabase(Context context) {
        for (Place place: mPlaces) {
            place.setIsInOwnership(checkIsInOwnership(context, place.getOwner()));
            place.writeToDatabase(context);
        }
        context.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
        Log.d(LOG_TAG, "places list updated");
    }

    private boolean checkIsInOwnership(Context context, Player player) {
         return player != null && player.getId()== AccountManagerHelper.getPlayerId(context);
    }


    public void setIsAroundThePoint(boolean isAroundThePoint) {
        for (Place place : mPlaces) {
            place.setIsAroundThePoint(isAroundThePoint);
        }
    }

    public void setIsAroundThePlayer(boolean isAroundThePlayer) {
        for (Place place : mPlaces) {
            place.setIsAroundThePlayer(isAroundThePlayer);
        }
    }

    public void setIsVisitedInThePast(boolean isVisitedInThePast) {
        for (Place place : mPlaces) {
            place.setIsVisitedInThePast(isVisitedInThePast);
        }
    }

    public void add(Place place) {
        mPlaces.add(place);
    }

    public void setIsInOwnership(boolean isInOwnership) {
        for (Place place: mPlaces) {
            place.setIsInOwnership(true);
        }
    }
}