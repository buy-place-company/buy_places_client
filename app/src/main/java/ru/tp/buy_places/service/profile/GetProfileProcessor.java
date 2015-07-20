package ru.tp.buy_places.service.profile;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Player;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 09.07.2015.
 */
public class GetProfileProcessor extends Processor {

    private static final String KEY_PROFILE = "KEY_PROFILE";

    protected GetProfileProcessor(Context context, OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject dataJSONArray = responseJSONObject.optJSONObject("user");
        Player player = Player.fromJSONObject(dataJSONArray);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_PROFILE, player);
        return new Response(status, message, data);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        String path = "/profile";
        return new Request(mContext, path, Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Player player = (Player)response.getData().get(KEY_PROFILE);
        player.writeToDatabase(mContext);
    }
}
