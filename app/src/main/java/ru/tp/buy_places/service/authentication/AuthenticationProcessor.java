package ru.tp.buy_places.service.authentication;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.network.UnknownErrorResponse;
import ru.tp.buy_places.service.resourses.AuthenticationResult;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 16.07.2015.
 */
public class AuthenticationProcessor extends Processor {
    public static final String KET_AUTHENTICATION_RESULT = "KEY_AUTHENTICATION_RESULT";
    private final String mCode;
    protected AuthenticationProcessor(Context context, OnProcessorResultListener listener, String code) {
        super(context, listener);
        mCode = code;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        if (responseJSONObject == null) {
            return new UnknownErrorResponse();
        }
        int status = responseJSONObject.optInt("code");
        String message = responseJSONObject.optString("message", null);
        Long id = responseJSONObject.optLong("id");
        String username = responseJSONObject.optString("name");
        AuthenticationResult result = new AuthenticationResult(id, username);
//        JSONObject jsonObject = responseJSONObject.optJSONObject("user");
//        Player result = Player.fromJSONObject(jsonObject);
        Map<String, Resource> data = new HashMap<>();
        data.put(KET_AUTHENTICATION_RESULT, result);
        return new Response(status, message, data);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("code", mCode);
        return new Request(mContext, "/auth/vk", Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        //AuthenticationResult result = (AuthenticationResult) response.getData();
        //long id = result.getId();
        //String username = result.getUsername();

//        Player player = (Player) response.getData();
//        player.writeToDatabase(mContext);
//        long id = player.getId();
//        String username = player.getUsername();
    }
}
