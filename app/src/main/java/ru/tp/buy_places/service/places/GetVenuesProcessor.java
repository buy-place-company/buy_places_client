package ru.tp.buy_places.service.places;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Places;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 20.07.2015.
 */
public abstract class GetVenuesProcessor extends Processor {
    protected static final String KEY_VENUES = "KEY_VENUES";

    protected GetVenuesProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("code");
        String message = responseJSONObject.optString("message", null);
        JSONArray dataJSONArray = responseJSONObject.optJSONArray("venues");
        Places places = Places.fromJSONArray(dataJSONArray);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_VENUES, places);
        return new Response(status, message, data);
    }
}
