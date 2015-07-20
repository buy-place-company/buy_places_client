package ru.tp.buy_places.service.places;

import android.content.ContentValues;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Places;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetVenuesAroundThePlayerProcessor extends GetVenuesProcessor {
    private final LatLng mPlayerPosition;

    public GetVenuesAroundThePlayerProcessor(Context context, Processor.OnProcessorResultListener listener, LatLng playerPosition) {
        super(context, listener);
        mPlayerPosition = playerPosition;
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("lng", Double.toString(mPlayerPosition.longitude));
        params.put("lat", Double.toString(mPlayerPosition.latitude));
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
            markPlacesAroundTheLastPlayerPosition(mContext);
            places.setIsAroundThePlayer(true);
            places.writeToDatabase(mContext);
        }
    }

    private void markPlacesAroundTheLastPlayerPosition(Context context) {
        ContentValues aroundThePlayerIsFalseContentValues = new ContentValues();
        aroundThePlayerIsFalseContentValues.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, false);
        context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, aroundThePlayerIsFalseContentValues, BuyPlacesContract.Places.AROUND_THE_PLAYER_SELECTION, null);
    }
}
