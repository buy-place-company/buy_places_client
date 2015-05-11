package ru.tp.buy_places.fragments.map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ru.tp.buy_places.R;

/**
 * Created by home on 11.05.2015.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    CustomInfoWindowAdapter(Activity context){
        this.context = context;
    };

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = context.getLayoutInflater().inflate(R.layout.object_infowindow, null);
        Button btnBuy = (Button) v.findViewById(R.id.button_buy_object);
        TextView tvName = (TextView) v.findViewById(R.id.text_view_object_custom_name);
        TextView tvPrice = (TextView) v.findViewById(R.id.text_view_object_custom_price);
        tvPrice.setText("Цена: 50$");
        tvName.setText("Название: МГТУ им Баумана");

        return v;
    }

}
