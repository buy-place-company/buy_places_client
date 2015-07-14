package ru.tp.buy_places.service.action_with_place;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

import static ru.tp.buy_places.service.BuyItService.ActionWithPlace;

/**
 * Created by Ivan on 10.05.2015.
 */
public class ActionWithPlaceProcessorCreator extends ProcessorCreator {
    private final String mId;
    private final ActionWithPlace mActionWithPlace;

    public ActionWithPlaceProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String id, ActionWithPlace actionWithPlace) {
        super(context, listener);
        mId = id;
        mActionWithPlace = actionWithPlace;
    }

    @Override
    public Processor createProcessor() {
        return new ActionWithPlaceProcessor(mContext, mListener, mId, mActionWithPlace);
    }
}
