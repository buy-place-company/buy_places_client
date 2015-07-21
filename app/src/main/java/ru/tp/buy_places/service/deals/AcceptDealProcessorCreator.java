package ru.tp.buy_places.service.deals;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 21.07.2015.
 */
public class AcceptDealProcessorCreator extends ProcessorCreator {
    private final long mDealId;

    public AcceptDealProcessorCreator(Context context, Processor.OnProcessorResultListener listener, long dealToAcceptId) {
        super(context, listener);
        mDealId = dealToAcceptId;
    }

    @Override
    public Processor createProcessor() {
        return new AcceptDealProcessor(mContext, mListener, mDealId);
    }
}
