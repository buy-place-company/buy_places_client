package ru.tp.buy_places.map.marker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 07.07.2015.
 */
public class ClusterItemMarkerOptionsCreator extends MarkerOptionsCreator {
    private String mTitle;

    public ClusterItemMarkerOptionsCreator(Context context) {
        super(context);
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    protected void setContentView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.place_cluster_item_marker, null);
        mIconGenerator.setContentView(view);
        mIconGenerator.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
    }

    @Override
    protected MarkerOptions applyMarkerOptions(MarkerOptions markerOptions) {
        if (mTitle != null)
            markerOptions.title(mTitle);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon()));

        return markerOptions;
    }
}
