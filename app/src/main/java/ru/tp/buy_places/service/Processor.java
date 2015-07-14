package ru.tp.buy_places.service;

import android.content.Context;

import org.json.JSONObject;

import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.network.UnknownErrorResponse;

/**
 * Created by Ivan on 22.04.2015.
 */
public abstract class Processor {
    protected final Context mContext;
    protected final OnProcessorResultListener mListener;

    protected Processor(Context context, OnProcessorResultListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void process() {
        Request request = prepareRequest();
        updateContentProviderBeforeExecutingRequest(request);
        JSONObject responseJSONObject = request.execute();
        Response response;
        if (responseJSONObject != null) {
            response = parseResponseJSONObject(responseJSONObject);
            updateContentProviderAfterExecutingRequest(response);
        }
        else {
            response = new UnknownErrorResponse();
        }
        mListener.send(response.getStatus());
    }

    abstract protected Response parseResponseJSONObject(JSONObject responseJSONObject);

    abstract protected Request prepareRequest();

    abstract protected void updateContentProviderBeforeExecutingRequest(Request request);

    abstract protected void updateContentProviderAfterExecutingRequest(Response response);

    public interface OnProcessorResultListener {
        void send(int result);
    }
}