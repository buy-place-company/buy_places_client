package ru.tp.buy_places.map.marker;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by Ivan on 07.07.2015.
 */
abstract public class MarkerOptionsCreator {
    protected final IconGenerator mIconGenerator;
    protected final Context mContext;
    protected LatLng mPosition;

    protected MarkerOptionsCreator(Context context) {
        mContext = context;
        mIconGenerator = new IconGenerator(context);
        setContentView();
    }

    public void setPosition(LatLng position) {
        mPosition = position;
    }


    public MarkerOptions create(MarkerOptions markerOptions) {
        if (markerOptions == null)
            markerOptions = new MarkerOptions();
        if (markerOptions.getPosition() == null) {
            markerOptions.position(mPosition);
        }
        if (markerOptions.getPosition() == null) {
            return null;
        }
        return applyMarkerOptions(markerOptions);
    }

    abstract protected void setContentView();

    abstract protected MarkerOptions applyMarkerOptions(MarkerOptions markerOptions);
}
