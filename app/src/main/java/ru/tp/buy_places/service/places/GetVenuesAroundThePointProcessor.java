package ru.tp.buy_places.service.places;

import android.content.ContentValues;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Places;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetVenuesAroundThePointProcessor extends GetVenuesProcessor {
    private final LatLng mPointPosition;

    public GetVenuesAroundThePointProcessor(Context context, OnProcessorResultListener listener, LatLng pointPosition) {
        super(context, listener);
        mPointPosition = pointPosition;
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("lng", Double.toString(mPointPosition.longitude));
        params.put("lat", Double.toString(mPointPosition.latitude));
        String path = "/zone/venues";
        return new Request(mContext, path, Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Places places = (Places) response.getData().get(KEY_VENUES);
        if (!places.isEmpty()) {
            deleteOrMarkPlacesAroundTheLastPoint(mContext);
            places.setIsAroundThePoint(true);
            places.writeToDatabase(mContext);
        }
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
    }

    private void deleteOrMarkPlacesAroundTheLastPoint(Context context) {
        ContentValues aroundThePointIsFalseContentValues = new ContentValues();
        aroundThePointIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePointIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_POINT_SELECTION, null);
    }
}
