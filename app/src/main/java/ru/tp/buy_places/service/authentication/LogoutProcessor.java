package ru.tp.buy_places.service.authentication;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 22.07.2015.
 */
public class LogoutProcessor extends Processor {
    public LogoutProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        return new Response(status, message, null);
    }

    @Override
    protected Request prepareRequest() {
        String path = "/auth/logout";
        return new Request(mContext, path, Request.RequestMethod.POST, new HashMap<String, String>());
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {

    }
}
