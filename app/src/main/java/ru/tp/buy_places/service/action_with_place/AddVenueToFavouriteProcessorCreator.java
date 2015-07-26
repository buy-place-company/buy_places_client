package ru.tp.buy_places.service.action_with_place;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 26.07.2015.
 */
public class AddVenueToFavouriteProcessorCreator extends ProcessorCreator {
    private final String mVenueId;

    public AddVenueToFavouriteProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String venueToAddId) {
        super(context, listener);
        mVenueId = venueToAddId;

    }

    @Override
    public Processor createProcessor() {
        return new AddToFavouriteProcessor(mContext, mListener, mVenueId);
    }
}
