package ru.tp.buy_places.map.marker;

import android.content.Context;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Ivan on 07.07.2015.
 */
public class PlayerLocationMarkerOptionsCreator extends MarkerOptionsCreator {

    public PlayerLocationMarkerOptionsCreator(Context context) {
        super(context);
    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected MarkerOptions applyMarkerOptions(MarkerOptions markerOptions) {

        return markerOptions;
    }
}
