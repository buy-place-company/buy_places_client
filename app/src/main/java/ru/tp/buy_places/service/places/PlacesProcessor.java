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
        String path;
        switch (mObjectsRequestMode) {
            case AROUND_THE_POINT:
            case AROUND_THE_PLAYER:
                params.put("lat", Double.toString(mPosition.latitude));
                params.put("lng", Double.toString(mPosition.longitude));
                path = "/objects";
                break;
            case IN_OWNERSHIP:
                path = "/user_objects";
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
        JSONArray dataJSONArray = responseJSONObject.optJSONArray("places");
        Places places = Places.fromJSONArray(dataJSONArray);
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
                deleteOrMarkPlacesAroundTheLastPoint(mContext);
                places.setIsAroundThePoint(true);
                places.writeToDatabase(mContext);
                break;
            case AROUND_THE_PLAYER:
                markPlacesAroundTheLastPlayerPosition(mContext);
                places.setIsAroundThePlayer(true);
                places.setIsVisitedInThePast(true);
                places.writeToDatabase(mContext);
                break;
            case IN_OWNERSHIP:
                markPlacesInOwnership(mContext);
                places.setIsInOwnership(true);
                places.writeToDatabase(mContext);
        }
    }

    private void markPlacesInOwnership(Context context) {
        ContentValues isInOwnershipIsFalseContentValues = new ContentValues();
        isInOwnershipIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_IN_OWNERSHIP, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, isInOwnershipIsFalseContentValues, BuyPlacesContract.Places.IS_IN_OWNERSHIP_SELECTION, null);
    }

    private void deleteOrMarkPlacesAroundTheLastPoint(Context context) {
        ContentValues aroundThePointIsFalseContentValues = new ContentValues();
        aroundThePointIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, false);
        context.getContentResolver().delete(BuyPlacesContract.Places.CONTENT_URI, BuyPlacesContract.Places.ONLY_AROUND_THE_POINT_SELECTION, null);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePointIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_POINT_SELECTION, null);
    }

    private void markPlacesAroundTheLastPlayerPosition(Context context) {
        ContentValues aroundThePlayerIsFalseContentValues = new ContentValues();
        aroundThePlayerIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePlayerIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_PLAYER_SELECTION, null);
    }
}