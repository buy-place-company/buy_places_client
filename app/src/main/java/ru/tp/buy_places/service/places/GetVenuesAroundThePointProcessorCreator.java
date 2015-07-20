package ru.tp.buy_places.service.places;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetVenuesAroundThePointProcessorCreator extends ProcessorCreator {
    private final LatLng mPointPosition;

    public GetVenuesAroundThePointProcessorCreator(Context context, Processor.OnProcessorResultListener listener, LatLng pointPosition) {
        super(context, listener);
        mPointPosition = pointPosition;
    }

    @Override
    public Processor createProcessor() {
        return new GetVenuesAroundThePointProcessor(mContext, mListener, mPointPosition);
    }
}
