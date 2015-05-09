package ru.tp.buy_places.service.places;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

import static ru.tp.buy_places.service.BuyItService.ObjectsRequestMode;

/**
 * Created by Ivan on 22.04.2015.
 */
public class PlacesProcessorCreator implements ProcessorCreator {

    private final LatLng mPosition;
    private final ObjectsRequestMode mObjectsRequestMode;
    private Context mContext;
    private Processor.OnProcessorResultListener mListener;


    public PlacesProcessorCreator(Context context, Processor.OnProcessorResultListener listener, LatLng position, ObjectsRequestMode objectsRequestMode) {
        mContext = context;
        mListener = listener;
        mPosition = position;
        mObjectsRequestMode = objectsRequestMode;
    }
    @Override
    public Processor createProcessor() {
        return new PlacesProcessor(mContext, mListener, mPosition, mObjectsRequestMode);
    }
}