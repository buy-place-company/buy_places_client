package ru.tp.buy_places.service.places;

import android.content.ContentValues;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Places;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetMyVenuesProcessor extends GetVenuesProcessor {
    public GetMyVenuesProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        String path = "/user/venues";
        return new Request(mContext, path, Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Places places = (Places) response.getData().get(KEY_VENUES);
        if (!places.isEmpty()) {
            markPlacesInOwnership(mContext);
            places.setIsInOwnership(true);
            places.writeToDatabase(mContext);
        }
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
    }

    private void markPlacesInOwnership(Context context) {
        ContentValues isInOwnershipIsFalseContentValues = new ContentValues();
        isInOwnershipIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_IN_OWNERSHIP, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, isInOwnershipIsFalseContentValues, BuyPlacesContract.Places.IS_IN_OWNERSHIP_SELECTION, null);
    }
}
