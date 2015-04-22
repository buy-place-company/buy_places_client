package ru.tp.buy_places;

import android.content.Context;

import org.json.JSONObject;

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
        Response response = parseResponseJSONObject(responseJSONObject);
        updateContentProviderAfterExecutingRequest(response);
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