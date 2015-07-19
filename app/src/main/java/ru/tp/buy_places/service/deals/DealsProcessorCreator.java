package ru.tp.buy_places.service.deals;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 16.07.2015.
 */
public class DealsProcessorCreator extends ProcessorCreator {
    public DealsProcessorCreator(Context context, Processor.OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    public Processor createProcessor() {
        return new DealsProcessor(mContext, mListener);
    }
}
