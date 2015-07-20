package ru.tp.buy_places.service.places;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetMyVenuesProcessorCreator extends ProcessorCreator{
    public GetMyVenuesProcessorCreator(Context context, Processor.OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    public Processor createProcessor() {
        return new GetMyVenuesProcessor(mContext, mListener);
    }
}
