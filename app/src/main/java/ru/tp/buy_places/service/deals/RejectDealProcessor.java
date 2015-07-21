package ru.tp.buy_places.service.deals;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.network.Request;

/**
 * Created by Ivan on 21.07.2015.
 */
public class RejectDealProcessor extends DealProcessor {
    private final long mDealId;

    public RejectDealProcessor(Context context, OnProcessorResultListener listener, long dealId) {
        super(context, listener);
        mDealId = dealId;
    }

    @Override
    protected Request prepareRequest() {
        String path = "/deals/cancel";
        Map<String, String> params = new HashMap<>();
        params.put("deal_id", String.valueOf(mDealId));
        return new Request(mContext, path, Request.RequestMethod.POST, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }
}
