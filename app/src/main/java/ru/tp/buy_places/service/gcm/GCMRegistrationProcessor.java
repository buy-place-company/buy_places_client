package ru.tp.buy_places.service.gcm;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 25.07.2015.
 */
public class GCMRegistrationProcessor extends Processor {
    private final String mToken;

    public GCMRegistrationProcessor(Context context, OnProcessorResultListener listener, String token) {
        super(context, listener);
        mToken = token;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        return new Response(status, message, null);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("reg_id", mToken);
        String path = "/push/reg";
        return new Request(mContext, path, Request.RequestMethod.POST, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {

    }
}
