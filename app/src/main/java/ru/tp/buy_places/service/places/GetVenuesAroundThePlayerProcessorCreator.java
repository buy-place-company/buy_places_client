package ru.tp.buy_places.service.places;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 20.07.2015.
 */
public class GetVenuesAroundThePlayerProcessorCreator extends ProcessorCreator {
    private final LatLng mPlayerPosition;

    public GetVenuesAroundThePlayerProcessorCreator(Context context, Processor.OnProcessorResultListener listener, LatLng playerPosition) {
        super(context, listener);
        mPlayerPosition = playerPosition;
    }

    @Override
    public Processor createProcessor() {
        return new GetVenuesAroundThePlayerProcessor(mContext, mListener, mPlayerPosition);
    }
}
