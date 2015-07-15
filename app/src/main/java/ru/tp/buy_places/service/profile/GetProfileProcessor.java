package ru.tp.buy_places.service.profile;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.network.UnknownErrorResponse;
import ru.tp.buy_places.service.resourses.Player;

/**
 * Created by Ivan on 09.07.2015.
 */
public class GetProfileProcessor extends Processor {

    protected GetProfileProcessor(Context context, OnProcessorResultListener listener, OnProcessorReceivedResponseListener onProcessorReceivedResponseListener, BuyItService.ResourceType resourceType, long requestId) {
        super(context, listener, onProcessorReceivedResponseListener, resourceType, requestId);
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        if (responseJSONObject == null) {
            return new UnknownErrorResponse();
        }
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject dataJSONArray = responseJSONObject.optJSONObject("user");
        Player player = Player.fromJSONObject(dataJSONArray);
        return new Response(status, message, player);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        String path = "/profile";
        return new Request(mContext, path, Request.RequestMethod.GET, params, this);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Player player = (Player)response.getData();
        player.writeToDatabase(mContext);
    }
}
