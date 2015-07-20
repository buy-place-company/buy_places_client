package ru.tp.buy_places.service.action_with_deal;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 20.07.2015.
 */
public class SuggestDealProcessorCreator extends ProcessorCreator {
    private final String mVenueId;
    private final long mAmount;

    public SuggestDealProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String venueId, long amount) {
        super(context, listener);
        mVenueId = venueId;
        mAmount = amount;
    }

    @Override
    public Processor createProcessor() {
        return new SuggestDealProcessor(mContext, mListener, mVenueId, mAmount);
    }
}
