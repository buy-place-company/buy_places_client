package ru.tp.buy_places.service;

import android.content.Context;

/**
 * Created by Ivan on 22.04.2015.
 */
public abstract class ProcessorCreator {
    protected final Context mContext;
    protected final Processor.OnProcessorResultListener mListener;
    protected final Processor.OnProcessorReceivedResponseListener mOnProcessorReceivedResponseListener;
    protected final BuyItService.ResourceType mResourceType;
    protected final long mRequestId;

    public ProcessorCreator(Context context, Processor.OnProcessorResultListener listener, Processor.OnProcessorReceivedResponseListener onProcessorReceivedResponseListener, BuyItService.ResourceType resourceType, long requestId) {
        mContext = context;
        mListener = listener;
        mOnProcessorReceivedResponseListener = onProcessorReceivedResponseListener;
        mResourceType = resourceType;
        mRequestId = requestId;
    }
    public abstract Processor createProcessor();
}