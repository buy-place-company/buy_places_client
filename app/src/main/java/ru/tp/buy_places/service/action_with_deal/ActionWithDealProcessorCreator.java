package ru.tp.buy_places.service.action_with_deal;

import android.content.Context;

import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 16.07.2015.
 */
public class ActionWithDealProcessorCreator extends ProcessorCreator {

    private final BuyItService.DealAction mDealAction;
    private final long mId;

    public ActionWithDealProcessorCreator(Context context, Processor.OnProcessorResultListener listener, BuyItService.DealAction dealAction, long id) {
        super(context, listener);
        mDealAction = dealAction;
        mId = id;
    }

    @Override
    public Processor createProcessor() {
        return new ActionWithDealProcessor(mContext, mListener, mDealAction, mId);
    }
}
