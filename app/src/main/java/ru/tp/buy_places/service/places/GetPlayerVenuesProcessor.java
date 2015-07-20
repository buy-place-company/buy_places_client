package ru.tp.buy_places.service.places;

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
public class GetPlayerVenuesProcessor extends GetVenuesProcessor {
    private final long mPlayerId;

    public GetPlayerVenuesProcessor(Context context, OnProcessorResultListener listener, long playerId) {
        super(context, listener);
        mPlayerId = playerId;
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", Long.toString(mPlayerId));
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
            places.writeToDatabase(mContext);
        }
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
    }
}
