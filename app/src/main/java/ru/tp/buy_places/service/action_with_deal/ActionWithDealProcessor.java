package ru.tp.buy_places.service.action_with_deal;

import android.content.Context;

import org.json.JSONObject;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 16.07.2015.
 */
public class ActionWithDealProcessor extends Processor {

    protected ActionWithDealProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        return null;
    }

    @Override
    protected Request prepareRequest() {
        return null;
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
    }
}
