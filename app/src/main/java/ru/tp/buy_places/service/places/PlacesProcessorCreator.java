package ru.tp.buy_places.service.places;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

import static ru.tp.buy_places.service.BuyItService.ObjectsRequestMode;

/**
 * Created by Ivan on 22.04.2015.
 */
public class PlacesProcessorCreator extends ProcessorCreator {

    private final LatLng mPosition;
    private final ObjectsRequestMode mObjectsRequestMode;


    public PlacesProcessorCreator(Context context, Processor.OnProcessorResultListener listener, LatLng position, ObjectsRequestMode objectsRequestMode) {
        super(context, listener);
        mPosition = position;
        mObjectsRequestMode = objectsRequestMode;
    }
    @Override
    public Processor createProcessor() {
        return new PlacesProcessor(mContext, mListener, mPosition, mObjectsRequestMode);
    }
}