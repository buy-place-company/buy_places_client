package ru.tp.buy_places.service.action_with_place;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 26.07.2015.
 */
public class RemoveVenueFromFavouriteProcessorCreator extends ProcessorCreator {
    private final String mVenueId;

    public RemoveVenueFromFavouriteProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String venueToRemoveId) {
        super(context, listener);
        mVenueId = venueToRemoveId;
    }

    @Override
    public Processor createProcessor() {
        return new RemoveFromFavouriteProcessor(mContext, mListener, mVenueId);
    }
}
