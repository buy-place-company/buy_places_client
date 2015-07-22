package ru.tp.buy_places.service.authentication;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 22.07.2015.
 */
public class RegistrationProcessorCreator extends ProcessorCreator {
    private final String mEmail;
    private final String mUsername;
    private final String mPassword;

    public RegistrationProcessorCreator(Context context, Processor.OnProcessorResultListener listener, String email, String username, String password) {
        super(context, listener);
        mEmail = email;
        mUsername = username;
        mPassword = password;
    }

    @Override
    public Processor createProcessor() {
        return new RegistrationProcessor(mContext, mListener, mEmail, mUsername, mPassword);
    }
}
