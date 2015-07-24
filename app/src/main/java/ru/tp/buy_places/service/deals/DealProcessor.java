package ru.tp.buy_places.service.deals;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Deal;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 21.07.2015.
 */
abstract public class DealProcessor extends Processor {
    protected static final String KEY_DEAL = "KEY_DEAL";

    protected DealProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
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
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Deal deal = (Deal) response.getData().get(KEY_DEAL);
        deal.writeToDatabase(mContext);
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Deals.CONTENT_URI, null);
        mContext.getContentResolver().notifyChange(BuyPlacesContract.Players.CONTENT_URI, null);
    }
}
