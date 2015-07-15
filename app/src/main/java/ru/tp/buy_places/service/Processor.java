package ru.tp.buy_places.service;

import android.content.Context;

import org.json.JSONObject;

import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 22.04.2015.
 */
public abstract class Processor implements Request.OnResponseReceivedListener {
    protected final Context mContext;
    protected final OnProcessorResultListener mListener;
    private final BuyItService.ResourceType mResourceType;
    private final OnProcessorReceivedResponseListener mOnProcessorReceivedResponseListener;
    private Long mRequestId;

    protected Processor(Context context, OnProcessorResultListener listener, OnProcessorReceivedResponseListener onProcessorReceivedResponseListener, BuyItService.ResourceType resourceType, long requestId) {
        mContext = context;
        mListener = listener;
        mOnProcessorReceivedResponseListener = onProcessorReceivedResponseListener;
        mResourceType = resourceType;
        mRequestId = requestId;
    }

    public void process() {
        Request request = prepareRequest();
        request.start();
    }

    abstract protected Response parseResponseJSONObject(JSONObject responseJSONObject);

    abstract protected Request prepareRequest();

    abstract protected void updateContentProviderBeforeExecutingRequest(Request request);

    abstract protected void updateContentProviderAfterExecutingRequest(Response response);

    public BuyItService.ResourceType getResourceType() {
        return mResourceType;
    }

    public Long getRequestId() {
        return mRequestId;
    }

    public interface OnProcessorResultListener {
        void send(int result);
    }

    public interface OnProcessorReceivedResponseListener {
        void onProcessorReceivedResponse(Processor processor, Response response);
    }

    @Override
    public void onResponseReceived(JSONObject jsonObject) {
        Response response = parseResponseJSONObject(jsonObject);
        mOnProcessorReceivedResponseListener.onProcessorReceivedResponse(this, response);
    }
}