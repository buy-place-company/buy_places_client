package ru.tp.buy_places.service.places;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

import static ru.tp.buy_places.service.BuyItService.ObjectsRequestMode;

/**
 * Created by Ivan on 22.04.2015.
 */
public class PlacesProcessorCreator extends ProcessorCreator {

    private final LatLng mPosition;
    private final ObjectsRequestMode mObjectsRequestMode;


    public PlacesProcessorCreator(Context context, Processor.OnProcessorResultListener listener, Processor.OnProcessorReceivedResponseListener onProcessorResponseListener, LatLng position, ObjectsRequestMode objectsRequestMode, BuyItService.ResourceType resourceType, long requestId) {
        super(context, listener, onProcessorResponseListener, resourceType, requestId);
        mPosition = position;
        mObjectsRequestMode = objectsRequestMode;
    }
    @Override
    public Processor createProcessor() {
        return new PlacesProcessor(mContext, mListener, mOnProcessorReceivedResponseListener, mPosition, mObjectsRequestMode, mResourceType, mRequestId);
    }
}