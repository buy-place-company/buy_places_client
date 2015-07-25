package ru.tp.buy_places.service.gcm;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 25.07.2015.
 */
public class GCMRegistrationProcessorCreator extends ProcessorCreator {
    private final String mToken;

    public GCMRegistrationProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String token) {
        super(context, listener);
        mToken = token;
    }

    @Override
    public Processor createProcessor() {
        return new GCMRegistrationProcessor(mContext, mListener, mToken);
    }
}
