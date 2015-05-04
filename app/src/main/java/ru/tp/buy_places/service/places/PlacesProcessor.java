package ru.tp.buy_places.service.places;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Places;

import static ru.tp.buy_places.service.BuyItService.ObjectsRequestMode;

/**
 * Created by Ivan on 19.04.2015.
 */
public class PlacesProcessor extends Processor {

    private final ObjectsRequestMode mObjectsRequestMode;
    private final Location mLocation;

    PlacesProcessor(Context context, OnProcessorResultListener listener, Location location, ObjectsRequestMode objectsRequestMode) {
        super(context, listener);
        mLocation = location;
        mObjectsRequestMode = objectsRequestMode;

    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", Double.toString(mLocation.getLatitude()));
        params.put("lng", Double.toString(mLocation.getLongitude()));
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
        List<Place> placeList = places.getPlaces();
        switch (mObjectsRequestMode) {
            case AROUND_THE_POINT:

                //All rows:
                //AROUND_THE_POINT -> delete

                //Received rows:
                //VISITED_IN_THE_PAST -> AROUND_THE_POINT
                //AROUND_THE_PLAYER -> AROUND_THE_PLAYER

                mContext.getContentResolver().delete(Uri.withAppendedPath(BuyPlacesContract.Places.CONTENT_URI, BuyPlacesContract.Places.AROUND_THE_POINT_DATA_SET), BuyPlacesContract.Places.WITH_SPECIFIED_STATE_SELECTION, new String[]{String.valueOf(BuyPlacesContract.Places.State.AROUND_THE_PLAYER)});
                for (Place place : placeList) {
                    String id = place.getId();
                    long checkinsCount = place.getCheckinsCount();
                    long usersCount = place.getUsersCount();
                    long tipCount = place.getTipCount();
                    String name = place.getName();
                    String category = place.getCategory();
                    String type = place.getType();
                    String level = place.getLevel();
                    long owner = place.getOwner();
                    long price = place.getPrice();
                    float latitude = place.getLatitude();
                    float longitude = place.getLongitude();

                    ContentValues values = new ContentValues();
                    values.put(BuyPlacesContract.Places.COLUMN_ID, id);
                    values.put(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT, checkinsCount);
                    values.put(BuyPlacesContract.Places.COLUMN_USERS_COUNT, usersCount);
                    values.put(BuyPlacesContract.Places.COLUMN_TIP_COUNT, tipCount);
                    values.put(BuyPlacesContract.Places.COLUMN_NAME, name);
                    values.put(BuyPlacesContract.Places.COLUMN_CATEGORY, category);
                    values.put(BuyPlacesContract.Places.COLUMN_TYPE, type);
                    values.put(BuyPlacesContract.Places.COLUMN_LEVEL, level);
                    values.put(BuyPlacesContract.Places.COLUMN_OWNER, owner);
                    values.put(BuyPlacesContract.Places.COLUMN_PRICE, price);
                    values.put(BuyPlacesContract.Places.COLUMN_LATITUDE, latitude);
                    values.put(BuyPlacesContract.Places.COLUMN_LONGITUDE, longitude);
                    values.put(BuyPlacesContract.Places.COLUMN_STATE, BuyPlacesContract.Places.State.AROUND_THE_POINT.name());
                    mContext.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
                }
                break;


            case AROUND_THE_PLAYER:
                //All rows
                //AROUND_THE_PLAYER -> VISITED_IN_THE_PAST

                //Received rows
                //VISITED_IN_THE_PAST -> AROUND_THE_PLAYER
                //AROUND_THE_POINT -> AROUND_THE_PLAYER

                ContentValues contentValues = new ContentValues();
                contentValues.put(BuyPlacesContract.Places.COLUMN_STATE, BuyPlacesContract.Places.State.VISITED_IN_THE_PAST.name());
                mContext.getContentResolver().update(Uri.withAppendedPath(BuyPlacesContract.Places.CONTENT_URI, BuyPlacesContract.Places.AROUND_THE_POINT_DATA_SET), contentValues, BuyPlacesContract.Places.WITH_SPECIFIED_STATE_SELECTION, new String[]{String.valueOf(BuyPlacesContract.Places.State.AROUND_THE_PLAYER)});
                for (Place place : placeList) {
                    String id = place.getId();
                    long checkinsCount = place.getCheckinsCount();
                    long usersCount = place.getUsersCount();
                    long tipCount = place.getTipCount();
                    String name = place.getName();
                    String category = place.getCategory();
                    String type = place.getType();
                    String level = place.getLevel();
                    long owner = place.getOwner();
                    long price = place.getPrice();
                    float latitude = place.getLatitude();
                    float longitude = place.getLongitude();

                    ContentValues values = new ContentValues();
                    values.put(BuyPlacesContract.Places.COLUMN_ID, id);
                    values.put(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT, checkinsCount);
                    values.put(BuyPlacesContract.Places.COLUMN_USERS_COUNT, usersCount);
                    values.put(BuyPlacesContract.Places.COLUMN_TIP_COUNT, tipCount);
                    values.put(BuyPlacesContract.Places.COLUMN_NAME, name);
                    values.put(BuyPlacesContract.Places.COLUMN_CATEGORY, category);
                    values.put(BuyPlacesContract.Places.COLUMN_TYPE, type);
                    values.put(BuyPlacesContract.Places.COLUMN_LEVEL, level);
                    values.put(BuyPlacesContract.Places.COLUMN_OWNER, owner);
                    values.put(BuyPlacesContract.Places.COLUMN_PRICE, price);
                    values.put(BuyPlacesContract.Places.COLUMN_LATITUDE, latitude);
                    values.put(BuyPlacesContract.Places.COLUMN_LONGITUDE, longitude);
                    values.put(BuyPlacesContract.Places.COLUMN_STATE, BuyPlacesContract.Places.State.AROUND_THE_PLAYER.name());
                    mContext.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
                }
                break;
        }
    }
}