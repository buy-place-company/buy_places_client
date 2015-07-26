package ru.tp.buy_places.service.action_with_place;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 25.07.2015.
 */
abstract public class VenueProcessor extends Processor {
    protected static final String KEY_VENUE = "KEY_VENUE";
    protected final String mVenueId;

    protected VenueProcessor(Context context, OnProcessorResultListener listener, String venueId) {
        super(context, listener);
        mVenueId = venueId;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject venueJSONObject = responseJSONObject.optJSONObject("venue");
        Place place = Place.fromJSONObject(venueJSONObject);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_VENUE, place);
        return new Response(status, message, data);
    }
}
