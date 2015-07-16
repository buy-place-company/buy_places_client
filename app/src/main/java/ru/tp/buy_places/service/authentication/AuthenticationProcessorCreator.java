package ru.tp.buy_places.service.authentication;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 16.07.2015.
 */
public class AuthenticationProcessorCreator extends ProcessorCreator {
    private final String mCode;

    public AuthenticationProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String code) {
        super(context, listener);
        mCode = code;
    }

    @Override
    public Processor createProcessor() {
        return new AuthenticationProcessor(mContext, mListener, mCode);
    }
}
