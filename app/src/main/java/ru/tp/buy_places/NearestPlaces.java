package ru.tp.buy_places;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19.04.2015.
 */
public class NearestPlaces {
    private List<Place> mNearestPlaces = new ArrayList<>();


    public NearestPlaces(JSONArray nearestPlacesData) {
            for (int i = 0; i < nearestPlacesData.length(); i++) {
                Place place = new Place(nearestPlacesData.optJSONObject(i));
                mNearestPlaces.add(place);
            }
    }

    public List<Place> getPlaces() {
        return mNearestPlaces;
    }
}
