package ru.tp.buy_places.service.resourses;


import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Places implements Resource {
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
    }

    private boolean checkIsInOwnership(Context context, Player id) {
        // TODO Request Account Manager player's id and compare with parameter
        if (id == null)
            return false;
        else if (id.getId() == 43)
            return true;
        return false;
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