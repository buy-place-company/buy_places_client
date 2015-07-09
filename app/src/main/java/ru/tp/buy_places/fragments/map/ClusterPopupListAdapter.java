package ru.tp.buy_places.fragments.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.map.VenueClusterItem;

/**
 * Created by Ivan on 09.07.2015.
 */
public class ClusterPopupListAdapter extends BaseAdapter {
    private final Context mContext;
    private List<VenueClusterItem> mList;

    static class ViewHolder {
        TextView nameTextView;
        ImageView imageTextView;
        TextView priceTextView;
        TextView checkInsTextView;
        TextView levelTextView;

    }

    public ClusterPopupListAdapter(Context context, List<VenueClusterItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView =LayoutInflater.from(mContext).inflate(R.layout.item_object_cluster_popup, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.title_place_list);
            viewHolder.imageTextView = (ImageView) convertView.findViewById(R.id.icon_place_list);
            viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.checkInsTextView = (TextView) convertView.findViewById(R.id.tv_chekins);
            viewHolder.levelTextView = (TextView) convertView.findViewById(R.id.tv_level);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        VenueClusterItem clusterItem = mList.get(position);
        if (clusterItem != null) {
            viewHolder.nameTextView.setText(clusterItem.getName());
            viewHolder.imageTextView.setImageResource(R.mipmap.ic_object);
            viewHolder.priceTextView.setText(Long.toString(clusterItem.getPrice()));
            viewHolder.checkInsTextView.setText(Long.toString(clusterItem.getCheckinsCount()));
            viewHolder.levelTextView.setText(Integer.toString(clusterItem.getLevel()));
        }
        return convertView;
    }
}
