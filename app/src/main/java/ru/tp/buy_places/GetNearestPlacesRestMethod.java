package ru.tp.buy_places;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by Ivan on 20.04.2015.
 */
public class GetNearestPlacesRestMethod extends AbstractRestMethod<NearestPlaces> {
    private Context mContext;

    private static final URI TIMELINE_URI = URI.create("https://api.twitter.com/1/statuses/home_timeline.json");


    public GetNearestPlacesRestMethod(Context context){
        mContext = context.getApplicationContext();
    }


    @Override
    protected Request buildRequest() {

        Request request = new Request(RestMethodFactory.Method.GET, TIMELINE_URI, null, null);
        return request;
    }


    @Override
    protected NearestPlaces parseResponseBody(String responseBody) throws Exception {

        JSONArray json = new JSONObject(responseBody).optJSONArray("data");
        return new NearestPlaces(json);

    }


    @Override
    protected Context getContext() {
        return mContext;
    }
}
