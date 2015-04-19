package ru.tp.buy_places;

import android.content.ContentValues;
import android.content.Context;


import java.util.List;

import static ru.tp.buy_places.content_provider.BuyPlacesContract.*;

/**
 * Created by Ivan on 19.04.2015.
 */
public class NearestPlacesProcessor {
    private Context mContext;

    public NearestPlacesProcessor(Context context) {
        mContext = context;
    }


    public void getNearestPlaces(NearestPlacesProcessorCallback callback) {
        RestMethod<NearestPlaces> getNearestPlacesMethod = RestMethodFactory.get(mContext)
                .getRestMethod(Places.CONTENT_URI, RestMethodFactory.Method.GET, null, null);
        RestMethodResult<NearestPlaces> result = getNearestPlacesMethod.execute();

        updateContentProvider(result);
        callback.send(result.getStatusCode());
    }

    private void updateContentProvider(RestMethodResult<NearestPlaces> result) {
        NearestPlaces timeline = result.getResource();
        List<Place> places = timeline.getPlaces();


        for (Place place : places) {
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
            values.put(Places.COLUMN_ID, id);
            values.put(Places.COLUMN_CHECKINS_COUNT, checkinsCount);
            values.put(Places.COLUMN_USERS_COUNT, usersCount);
            values.put(Places.COLUMN_TIP_COUNT, tipCount);
            values.put(Places.COLUMN_NAME, name);
            values.put(Places.COLUMN_CATEGORY, category);
            values.put(Places.COLUMN_TYPE, type);
            values.put(Places.COLUMN_LEVEL, level);
            values.put(Places.COLUMN_OWNER, owner);
            values.put(Places.COLUMN_PRICE, price);
            values.put(Places.COLUMN_LATITUDE, latitude);
            values.put(Places.COLUMN_LONGITUDE, longitude);
            mContext.getContentResolver().insert(Places.CONTENT_URI, values);
        }
    }
}
