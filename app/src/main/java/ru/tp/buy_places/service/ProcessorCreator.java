package ru.tp.buy_places.service;

import android.content.Context;

/**
 * Created by Ivan on 22.04.2015.
 */
public abstract class ProcessorCreator {
    protected final Context mContext;
    protected final Processor.OnProcessorResultListener mListener;

    public ProcessorCreator(Context context, Processor.OnProcessorResultListener listener) {
        mContext = context;
        mListener = listener;
    }
    public abstract Processor createProcessor();
}