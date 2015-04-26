package ru.tp.buy_places.service.places;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 22.04.2015.
 */
public class PlacesProcessorCreator implements ProcessorCreator {

    private Context mContext;
    private Processor.OnProcessorResultListener mListener;
    private double mLatitude;
    private double mLongitude;

    public PlacesProcessorCreator(Context context, Processor.OnProcessorResultListener listener, double latitude, double longitude) {
        mContext = context;
        mListener = listener;
        mLatitude = latitude;
        mLongitude = longitude;
    }
    @Override
    public Processor createProcessor() {
        return new PlacesProcessor(mContext, mListener, mLatitude, mLongitude);
    }
}