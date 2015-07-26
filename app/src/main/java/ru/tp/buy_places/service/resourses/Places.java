package ru.tp.buy_places.service.resourses;


import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Places implements Resource, Iterable<Place> {
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
        if (placesJSONArray == null)
            return null;
        Places places = new Places();
        for (int i = 0; i < placesJSONArray.length(); i++) {
            Place place = Place.fromJSONObject(placesJSONArray.optJSONObject(i));
            places.add(place);
        }
        return places;
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }


    public void writeToDatabase(Context context) {
        for (Place place: mPlaces) {
            place.setIsInOwnership(Place.checkIsInOwnership(context, place.getOwner()));
            place.writeToDatabase(context);
        }
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

    public void add(Place place) {
        mPlaces.add(place);
    }

    public void setIsInOwnership(boolean isInOwnership) {
        for (Place place: mPlaces) {
            place.setIsInOwnership(isInOwnership);
        }
    }

    public boolean isEmpty() {
        return mPlaces.isEmpty();
    }

    @Override
    public Iterator<Place> iterator() {
        return mPlaces.iterator();
    }

}