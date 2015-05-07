package ru.tp.buy_places.service.resourses;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Places implements Resource {
    private List<Place> mNearestObjects = new ArrayList<>();


    public Places(JSONArray placesJSONObject) {
        if (placesJSONObject == null)
            mNearestObjects = null;
        else {
            for (int i = 0; i < placesJSONObject.length(); i++) {
                Place object = new Place(placesJSONObject.optJSONObject(i));
                mNearestObjects.add(object);
            }
        }
    }

    public List<Place> getPlaces() {
        return mNearestObjects;
    }
}
