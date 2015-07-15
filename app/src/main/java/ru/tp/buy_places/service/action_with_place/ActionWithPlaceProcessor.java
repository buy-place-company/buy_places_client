package ru.tp.buy_places.service.action_with_place;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Place;

import static ru.tp.buy_places.service.BuyItService.ActionWithPlace;

/**
 * Created by Ivan on 10.05.2015.
 */
public class ActionWithPlaceProcessor extends Processor {
    private final String mId;
    private final ActionWithPlace mActionWithPlace;

    public ActionWithPlaceProcessor(Context context, OnProcessorResultListener listener, OnProcessorReceivedResponseListener onProcessorReceivedResponseListener, String id, ActionWithPlace actionWithPlace, BuyItService.ResourceType resourceType, long requestId) {
        super(context, listener, onProcessorReceivedResponseListener, resourceType, requestId);
        mId = id;
        mActionWithPlace = actionWithPlace;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("code");
        String message = responseJSONObject.optString("message", null);
        JSONObject dataJSONObject = responseJSONObject.optJSONObject("data");
        Place place = Place.fromJSONObject(dataJSONObject);
        return new Response(status, message, place);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("id", mId);
        params.put("action", mActionWithPlace.name().toLowerCase());
        return new Request(mContext, "/object", Request.RequestMethod.POST, params, this);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {
        ContentValues stateUpdatingIsTrue = new ContentValues();
        stateUpdatingIsTrue.put(BuyPlacesContract.Places.COLUMN_STATE_UPDATING, true);
        mContext.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, stateUpdatingIsTrue, BuyPlacesContract.Places.WITH_SPECIFIED_ID_SELECTION, new String[]{mId});
    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Place place = (Place) response.getData();
        place.setStateUpdating(false);
        place.writeToDatabase(mContext);
    }
}
