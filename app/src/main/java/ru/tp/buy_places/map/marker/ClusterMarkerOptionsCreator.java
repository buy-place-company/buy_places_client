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
public class ClusterMarkerOptionsCreator extends MarkerOptionsCreator {

    private int mItemsCount;

    public ClusterMarkerOptionsCreator(Context context) {
        super(context);

    }

    public void setItemsCount(int itemsCount) {
        mItemsCount = itemsCount;
    }

    @Override
    protected void setContentView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.places_cluster_marker, null);
        mIconGenerator.setContentView(view);
        mIconGenerator.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
    }

    @Override
    protected MarkerOptions applyMarkerOptions(MarkerOptions markerOptions) {
        if (mItemsCount > 0) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(Integer.toString(mItemsCount))));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon()));
        }
        return markerOptions;
    }
}
