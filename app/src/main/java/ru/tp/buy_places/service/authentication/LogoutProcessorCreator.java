package ru.tp.buy_places.service.authentication;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 22.07.2015.
 */
public class LogoutProcessorCreator extends ProcessorCreator {
    public LogoutProcessorCreator(Context context, Processor.OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    public Processor createProcessor() {
        return new LogoutProcessor(mContext, mListener);
    }
}
