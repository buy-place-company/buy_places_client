package ru.tp.buy_places.service.places;

import android.content.ContentValues;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.network.UnknownErrorResponse;
import ru.tp.buy_places.service.resourses.Places;
import ru.tp.buy_places.service.resourses.Resource;

import static ru.tp.buy_places.service.BuyItService.ObjectsRequestMode;

/**
 * Created by Ivan on 19.04.2015.
 */
public class PlacesProcessor extends Processor {

    private static final String KEY_VENUES = "KEY_VENUES";
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
        String path;
        switch (mObjectsRequestMode) {
            case AROUND_THE_POINT:
            case AROUND_THE_PLAYER:
                params.put("lng", Double.toString(mPosition.longitude));
                params.put("lat", Double.toString(mPosition.latitude));
                path = "/zone/venues";
                break;
            case IN_OWNERSHIP:
                path = "/user/venues";
                break;
            default:
                path = null;
        }
        return new Request(mContext, path, Request.RequestMethod.GET, params);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        if (responseJSONObject == null) {
            return new UnknownErrorResponse();
        }
        int status = responseJSONObject.optInt("code");
        String message = responseJSONObject.optString("message", null);
        JSONArray dataJSONArray = responseJSONObject.optJSONArray("venues");
        Places places = Places.fromJSONArray(dataJSONArray);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_VENUES, places);
        return new Response(status, message, data);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response result) {
        Places places = (Places) result.getData().get(KEY_VENUES);
        switch (mObjectsRequestMode) {
            case AROUND_THE_POINT:
                if (!places.isEmpty()) {
                    deleteOrMarkPlacesAroundTheLastPoint(mContext);
                    places.setIsAroundThePoint(true);
                    places.writeToDatabase(mContext);
                }
                break;
            case AROUND_THE_PLAYER:
                if (!places.isEmpty()) {
                    markPlacesAroundTheLastPlayerPosition(mContext);
                    places.setIsAroundThePlayer(true);
                    places.writeToDatabase(mContext);
                }
                break;
            case IN_OWNERSHIP:
                if (!places.isEmpty()) {
                    markPlacesInOwnership(mContext);
                    places.setIsInOwnership(true);
                    places.writeToDatabase(mContext);
                }
        }
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
    }

    private void markPlacesInOwnership(Context context) {
        ContentValues isInOwnershipIsFalseContentValues = new ContentValues();
        isInOwnershipIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_IN_OWNERSHIP, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, isInOwnershipIsFalseContentValues, BuyPlacesContract.Places.IS_IN_OWNERSHIP_SELECTION, null);
    }

    private void deleteOrMarkPlacesAroundTheLastPoint(Context context) {
        ContentValues aroundThePointIsFalseContentValues = new ContentValues();
        aroundThePointIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePointIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_POINT_SELECTION, null);
    }

    private void markPlacesAroundTheLastPlayerPosition(Context context) {
        ContentValues aroundThePlayerIsFalseContentValues = new ContentValues();
        aroundThePlayerIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePlayerIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_PLAYER_SELECTION, null);
    }
}