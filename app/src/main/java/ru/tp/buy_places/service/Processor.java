package ru.tp.buy_places.service;

import android.content.Context;

import org.json.JSONObject;

import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

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
        try {
            Request request = prepareRequest();
            updateContentProviderBeforeExecutingRequest(request);
            JSONObject responseJSONObject = request.execute();
            Response response = parseResponseJSONObject(responseJSONObject);
            if (response.getStatus() == 200) {
                updateContentProviderAfterExecutingRequest(response);
            }
            mListener.send(response);
        } catch (Exception e) {
            mListener.send(new Response(0, null, null));
        }

    }

    abstract protected Response parseResponseJSONObject(JSONObject responseJSONObject);

    abstract protected Request prepareRequest();

    abstract protected void updateContentProviderBeforeExecutingRequest(Request request);

    abstract protected void updateContentProviderAfterExecutingRequest(Response response);

    public interface OnProcessorResultListener {
        void send(Response response);
    }
}