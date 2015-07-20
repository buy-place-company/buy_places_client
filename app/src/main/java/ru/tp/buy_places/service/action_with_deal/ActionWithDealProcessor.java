package ru.tp.buy_places.service.action_with_deal;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Deal;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 16.07.2015.
 */
public class ActionWithDealProcessor extends Processor {

    private static final String KEY_DEAL = "KEY_DEAL";
    private final BuyItService.DealAction mDealAction;
    private final long mId;

    protected ActionWithDealProcessor(Context context, OnProcessorResultListener listener, BuyItService.DealAction dealAction, long id) {
        super(context, listener);
        mDealAction = dealAction;
        mId = id;
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
        params.put("deal_id", Long.toString(mId));
        return new Request(mContext, "/deals" + mDealAction.toPath(), Request.RequestMethod.POST, params);
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
