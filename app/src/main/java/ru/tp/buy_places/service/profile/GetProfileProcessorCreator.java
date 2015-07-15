package ru.tp.buy_places.service.profile;

import android.content.Context;

import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 09.07.2015.
 */
public class GetProfileProcessorCreator extends ProcessorCreator {

    public GetProfileProcessorCreator(Context context, Processor.OnProcessorResultListener listener, Processor.OnProcessorReceivedResponseListener onProcessorReceivedResponseListener, BuyItService.ResourceType resourceType, long requestId) {
        super(context, listener, onProcessorReceivedResponseListener, resourceType, requestId);
    }

    @Override
    public Processor createProcessor() {
        return new GetProfileProcessor(mContext, mListener, mOnProcessorReceivedResponseListener, mResourceType, mRequestId);
    }
}
