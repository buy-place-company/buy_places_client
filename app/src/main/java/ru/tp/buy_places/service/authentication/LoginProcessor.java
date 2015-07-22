package ru.tp.buy_places.service.authentication;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.AuthenticationResult;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 22.07.2015.
 */
abstract public class LoginProcessor extends Processor {
    public static final String KET_AUTHENTICATION_RESULT = "KEY_AUTHENTICATION_RESULT";

    protected LoginProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("code");
        String message = responseJSONObject.optString("message", null);
        Long id = responseJSONObject.optLong("id");
        String username = responseJSONObject.optString("name");
        AuthenticationResult result = new AuthenticationResult(id, username);
        Map<String, Resource> data = new HashMap<>();
        data.put(KET_AUTHENTICATION_RESULT, result);
        return new Response(status, message, data);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
    }
}
