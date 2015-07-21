package ru.tp.buy_places.service.deals;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Deal;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 20.07.2015.
 */
public class SuggestDealProcessor extends Processor {
    private static final String KEY_DEAL = "KEY_DEAL";
    private final String mVenueId;
    private final long mAmount;

    public SuggestDealProcessor(Context context, OnProcessorResultListener listener, String venueId, long amount) {
        super(context, listener);
        mVenueId = venueId;
        mAmount = amount;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject dealJsonObject = responseJSONObject.optJSONObject("deal");
        Deal deal = Deal.fromJSONObject(dealJsonObject);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_DEAL, deal);
        return new Response(status, message, data);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("venue_id", mVenueId);
        params.put("amount", Long.toString(mAmount));
        return new Request(mContext, "/deals/new", Request.RequestMethod.POST, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Deal deal = (Deal) response.getData().get(KEY_DEAL);
        deal.writeToDatabase(mContext);
    }
}
