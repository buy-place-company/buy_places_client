package ru.tp.buy_places.service.action_with_place;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Place;

/**
 * Created by Ivan on 25.07.2015.
 */
public class AddToFavouriteProcessor extends VenueProcessor {


    protected AddToFavouriteProcessor(Context context, OnProcessorResultListener listener, String venueId) {
        super(context, listener, venueId);
    }

    @Override
    protected Request prepareRequest() {
        String path = "/bookmark/new";
        Map<String, String> params = new HashMap<>();
        params.put("venue_id", mVenueId);
        return new Request(mContext, path, Request.RequestMethod.POST, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Place place = (Place) response.getData().get(KEY_VENUE);
        place.writeToDatabase(mContext);
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
    }
}
