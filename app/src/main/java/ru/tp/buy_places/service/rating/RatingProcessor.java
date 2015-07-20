package ru.tp.buy_places.service.rating;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Player;
import ru.tp.buy_places.service.resourses.Players;
import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 20.07.2015.
 */
public class RatingProcessor extends Processor {
    private static final String KEY_USER = "KEY_USER";
    private static final String KEY_USERS = "KEY_USERS";
    private final long mLimit;
    private final long mOffset;

    public RatingProcessor(Context context, OnProcessorResultListener listener, long limit, long offset) {
        super(context, listener);
        mLimit = limit;
        mOffset = offset;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject userJsonObject = responseJSONObject.optJSONObject("user");
        JSONArray usersJsonArray = responseJSONObject.optJSONArray("users");
        Player user = Player.fromJSONObject(userJsonObject);
        Players players = Players.fromJsonArray(usersJsonArray);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_USER, user);
        data.put(KEY_USERS, players);
        return new Response(status, message,data);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(mLimit));
        params.put("offset", String.valueOf(mOffset));
        final String path = "/user/rating";
        return new Request(mContext, path, Request.RequestMethod.GET, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {

    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Player user = (Player) response.getData().get(KEY_USER);
        Players users = (Players) response.getData().get(KEY_USERS);
        resetPositions(mContext);
        user.writeToDatabase(mContext);
        users.writeToDatabase(mContext, mOffset);

    }

    private void resetPositions(Context context) {
        ContentValues positionIsNullContentValues = new ContentValues();
        positionIsNullContentValues.put(BuyPlacesContract.Players.COLUMN_POSITION, false);
        context.getContentResolver().update(BuyPlacesContract.Players.CONTENT_URI, positionIsNullContentValues, BuyPlacesContract.Players.WITH_LOW_POSITION_LIMIT_SELECTION, new String[]{String.valueOf(mOffset)});
    }
}
