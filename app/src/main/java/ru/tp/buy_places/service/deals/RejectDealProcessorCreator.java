package ru.tp.buy_places.service.deals;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 21.07.2015.
 */
public class RejectDealProcessorCreator extends ProcessorCreator {
    private final long mDealId;

    public RejectDealProcessorCreator(Context context, Processor.OnProcessorResultListener listener, long dealId) {
        super(context, listener);
        mDealId = dealId;
    }

    @Override
    public Processor createProcessor() {
        return new RejectDealProcessor(mContext, mListener, mDealId);
    }
}
