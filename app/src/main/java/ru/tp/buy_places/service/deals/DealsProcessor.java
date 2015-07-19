package ru.tp.buy_places.service.deals;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.DealsFromServer;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 16.07.2015.
 */
public class DealsProcessor extends Processor {
    private static final String KEY_DEALS_FROM_SERVER = "KEY_DEALS_FROM_SERVER";

    protected DealsProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject jsonObject = responseJSONObject.optJSONObject("deals");
        DealsFromServer dealsFromServer = DealsFromServer.fromJsonObject(jsonObject);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_DEALS_FROM_SERVER, dealsFromServer);
        return new Response(status, message, data);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        return new Request(mContext, "/user/deals", Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        DealsFromServer dealsFromServer = (DealsFromServer) response.getData().get(KEY_DEALS_FROM_SERVER);
        dealsFromServer.writeToDatabase(mContext);
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Deals.CONTENT_URI, null);
    }
}
