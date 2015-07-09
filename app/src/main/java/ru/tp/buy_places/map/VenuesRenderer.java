package ru.tp.buy_places.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import ru.tp.buy_places.map.marker.ClusterItemMarkerOptionsCreator;
import ru.tp.buy_places.map.marker.ClusterMarkerOptionsCreator;

/**
 * Created by Ivan on 05.05.2015.
 */
public class VenuesRenderer extends DefaultClusterRenderer<VenueClusterItem> {
    private final ClusterMarkerOptionsCreator mClusterMarkerOptionsCreator;
    private final OnClusterItemRenderedListener mOnClusterItemRenderedListener;
    private final ClusterItemMarkerOptionsCreator mClusterMarkerItemOptionsCreator;

    @Override
    public void onRemove() {
        super.onRemove();
    }

    public VenuesRenderer(Context context, GoogleMap map, ClusterManager<VenueClusterItem> clusterManager, OnClusterItemRenderedListener onClusterItemRenderedListener) {
        super(context, map, clusterManager);
        mClusterMarkerItemOptionsCreator = new ClusterItemMarkerOptionsCreator(context);
        mClusterMarkerOptionsCreator = new ClusterMarkerOptionsCreator(context);
        mOnClusterItemRenderedListener = onClusterItemRenderedListener;
    }

    @Override
    protected void onClusterItemRendered(VenueClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        mOnClusterItemRenderedListener.onClusterItemRendered(clusterItem, marker);
    }



    @Override
    protected void onBeforeClusterItemRendered(VenueClusterItem item, MarkerOptions markerOptions) {
        //mClusterMarkerItemOptionsCreator.setTitle(item.getName());
        //mClusterMarkerItemOptionsCreator.create(markerOptions);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<VenueClusterItem> cluster, MarkerOptions markerOptions) {
        //mClusterMarkerOptionsCreator.setItemsCount(cluster.getSize());
        //mClusterMarkerOptionsCreator.create(markerOptions);
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<VenueClusterItem> cluster) {
        return cluster.getSize() > 2;
    }

    public interface OnClusterItemRenderedListener {
        void onClusterItemRendered(VenueClusterItem clusterItem, Marker marker);
    }
}
