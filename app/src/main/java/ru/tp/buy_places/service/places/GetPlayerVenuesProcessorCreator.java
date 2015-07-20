package ru.tp.buy_places.service.places;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetPlayerVenuesProcessorCreator extends ProcessorCreator {
    private final long mPlayerId;

    public GetPlayerVenuesProcessorCreator(Context context, Processor.OnProcessorResultListener listener, long playerId) {
        super(context, listener);
        mPlayerId = playerId;

    }

    @Override
    public Processor createProcessor() {
        return new GetPlayerVenuesProcessor(mContext, mListener, mPlayerId);
    }
}
