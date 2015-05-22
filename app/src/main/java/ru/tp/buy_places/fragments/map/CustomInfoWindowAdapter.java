package ru.tp.buy_places.fragments.map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.PlaceActivity;
import ru.tp.buy_places.map.ObjectRenderer;
import ru.tp.buy_places.map.PlaceClusterItem;

/**
 * Created by home on 11.05.2015.
 */
public class CustomInfoWindowAdapter implements  GoogleMap.InfoWindowAdapter, ObjectRenderer.OnClusterItemRenderedListener {

    private final Context mContext;
    private Map<Marker, PlaceClusterItem> mMarkerToPlaceClusterItem = new HashMap<>();


    public CustomInfoWindowAdapter(Context context){
        mContext = context;
    };

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }


    @Override
    public View getInfoContents(Marker marker) {
        if (mMarkerToPlaceClusterItem.containsKey(marker)) {
            PlaceClusterItem mItem = mMarkerToPlaceClusterItem.get(marker);
            if (mItem != null) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.object_infowindow, null);

                TextView tvName = (TextView) v.findViewById(R.id.text_view_object_custom_name);
                TextView tvPrice = (TextView) v.findViewById(R.id.text_view_object_custom_price);
                tvPrice.setText("Цена: "+ Long.toString(mItem.getPrice()));

                tvName.setText(mItem.getName());

                return v;
            }
        }
        return null;
    }

    @Override
    public void onClusterItemRendered(PlaceClusterItem clusterItem, Marker marker) {
        mMarkerToPlaceClusterItem.put(marker, clusterItem);
    }

    public PlaceClusterItem getItem(Marker marker) {
        if (mMarkerToPlaceClusterItem.containsKey(marker)) {
            return mMarkerToPlaceClusterItem.get(marker);
        }
        return null;
    }
}
