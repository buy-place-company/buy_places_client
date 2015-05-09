package ru.tp.buy_places.service.places;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Places;

import static ru.tp.buy_places.service.BuyItService.ObjectsRequestMode;

/**
 * Created by Ivan on 19.04.2015.
 */
public class PlacesProcessor extends Processor {

    private final ObjectsRequestMode mObjectsRequestMode;
    private final LatLng mPosition;

    PlacesProcessor(Context context, OnProcessorResultListener listener, LatLng position, ObjectsRequestMode objectsRequestMode) {
        super(context, listener);
        mPosition = position;
        mObjectsRequestMode = objectsRequestMode;

    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", Double.toString(mPosition.latitude));
        params.put("lng", Double.toString(mPosition.longitude));
        return new Request(mContext, "/objects_near", Request.RequestMethod.GET, params);
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
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response result) {
        Places places = (Places) result.getData();
        switch (mObjectsRequestMode) {
            case AROUND_THE_POINT:
                places.writeToDatabaseAsPlacesAroundThePoint(mContext);
                break;
            case AROUND_THE_PLAYER:
                places.writeToDatabaseAsPlacesAroundThePlayer(mContext);
                break;
        }
    }
}