package ru.tp.buy_places.service.authentication;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 22.07.2015.
 */
public class BaseLoginProcessorCreator extends ProcessorCreator {
    private final String mUsername;
    private final String mPassword;

    public BaseLoginProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String username, String password) {
        super(context, listener);
        mUsername = username;
        mPassword = password;
    }

    @Override
    public Processor createProcessor() {
        return new BaseLoginProcessor(mContext, mListener, mUsername, mPassword);
    }
}
