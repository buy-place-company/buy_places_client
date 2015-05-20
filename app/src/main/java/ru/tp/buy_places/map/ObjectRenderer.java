package ru.tp.buy_places.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 05.05.2015.
 */
public class ObjectRenderer extends DefaultClusterRenderer<PlaceClusterItem> {
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterItemGenerator;
    private final OnClusterItemRenderedListener mOnClusterItemRenderedListerer;


    public ObjectRenderer(Context context, GoogleMap map, ClusterManager<PlaceClusterItem> clusterManager, OnClusterItemRenderedListener onClusterItemRenderedListener) {
        super(context, map, clusterManager);

        mIconGenerator = new IconGenerator(context);
//        View placeClusterMarkerLayout = LayoutInflater.from(context).inflate(R.layout.places_cluster_marker, null);
//        mIconGenerator.setContentView(placeClusterMarkerLayout);
//
        mClusterItemGenerator = new IconGenerator(context);
//        View placesClusterItemMarkerLayout = LayoutInflater.from(context).inflate(R.layout.place_cluster_item_marker, null);
//        mClusterItemGenerator.setContentView(placesClusterItemMarkerLayout);
        mOnClusterItemRenderedListerer = onClusterItemRenderedListener;
    }

    @Override
    protected void onClusterItemRendered(PlaceClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        mOnClusterItemRenderedListerer.onClusterItemRendered(clusterItem, marker);
    }



    @Override
    protected void onBeforeClusterItemRendered(PlaceClusterItem item, MarkerOptions markerOptions) {
        mClusterItemGenerator.setColor(R.style.ClusterItemBackgroundColor);
        Bitmap icon = mClusterItemGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getName());

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<PlaceClusterItem> cluster, MarkerOptions markerOptions) {
        mIconGenerator.setColor(R.style.ClusterRenderBackgroundColor);
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<PlaceClusterItem> cluster) {
        return cluster.getSize() > 2;
    }

    public interface OnClusterItemRenderedListener {
        void onClusterItemRendered(PlaceClusterItem clusterItem, Marker marker);
    }
}
