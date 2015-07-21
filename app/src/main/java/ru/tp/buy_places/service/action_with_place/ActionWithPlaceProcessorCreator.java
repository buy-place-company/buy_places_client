package ru.tp.buy_places.service.action_with_place;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

import static ru.tp.buy_places.service.BuyItService.VenueAction;

/**
 * Created by Ivan on 10.05.2015.
 */
public class ActionWithPlaceProcessorCreator extends ProcessorCreator {
    private final String mId;
    private final VenueAction mVenueAction;

    public ActionWithPlaceProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String id, VenueAction venueAction) {
        super(context, listener);
        mId = id;
        mVenueAction = venueAction;
    }

    @Override
    public Processor createProcessor() {
        return new ActionWithPlaceProcessor(mContext, mListener, mId, mVenueAction);
    }
}
