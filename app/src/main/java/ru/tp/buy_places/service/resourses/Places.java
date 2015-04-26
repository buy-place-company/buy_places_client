package ru.tp.buy_places.service.resourses;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Places implements Resource {
    private List<Place> mNearestPlaces = new ArrayList<>();


    public Places(JSONArray placesJSONObject) {
        if (placesJSONObject == null)
            mNearestPlaces = null;
        else {
            for (int i = 0; i < placesJSONObject.length(); i++) {
                Place place = new Place(placesJSONObject.optJSONObject(i));
                mNearestPlaces.add(place);
            }
        }
    }

    public List<Place> getPlaces() {
        return mNearestPlaces;
    }
}
