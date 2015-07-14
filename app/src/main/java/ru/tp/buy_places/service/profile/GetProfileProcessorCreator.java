package ru.tp.buy_places.service.profile;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 09.07.2015.
 */
public class GetProfileProcessorCreator extends ProcessorCreator {

    public GetProfileProcessorCreator(Context context, Processor.OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    public Processor createProcessor() {
        return new GetProfileProcessor(mContext, mListener);
    }
}
