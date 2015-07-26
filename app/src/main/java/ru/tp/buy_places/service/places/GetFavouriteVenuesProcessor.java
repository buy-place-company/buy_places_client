package ru.tp.buy_places.service.places;

import android.content.Context;

import java.util.HashMap;

import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Places;

/**
 * Created by Ivan on 25.07.2015.
 */
public class GetFavouriteVenuesProcessor extends GetVenuesProcessor {
    public GetFavouriteVenuesProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Request prepareRequest() {
        String path = "/user/bookmarks";
        return new Request(mContext, path, Request.RequestMethod.GET, new HashMap<String, String>());
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Places places = (Places) response.getData().get(KEY_VENUES);
        places.setFavourite(true);
        places.writeToDatabase(mContext);
    }
}
