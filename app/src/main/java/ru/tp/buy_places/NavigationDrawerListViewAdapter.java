package ru.tp.buy_places;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.activities.MainActivity;

/**
 * Created by Ivan on 23.04.2015.
 */
public class NavigationDrawerListViewAdapter extends BaseAdapter {
    private final List<NavigationDrawerListViewItem> items = new ArrayList<>();
    private final Context mContext;

    public NavigationDrawerListViewAdapter(Context context) {
        mContext = context;
        items.add(new NavigationDrawerListViewItem(R.string.maps, R.mipmap.ic_map, MainActivity.Page.MAP));
        items.add(new NavigationDrawerListViewItem(R.string.myobjects, R.mipmap.ic_object, MainActivity.Page.MY_OBJECTS));
        items.add(new NavigationDrawerListViewItem(R.string.deals, R.mipmap.ic_deals, MainActivity.Page.DEALS));
        items.add(new NavigationDrawerListViewItem(R.string.raiting, R.mipmap.ic_raiting, MainActivity.Page.RATING));
        items.add(new NavigationDrawerListViewItem(R.string.settings, R.mipmap.ic_settings, MainActivity.Page.SETTINGS));
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).page.ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_navigation, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTextView.setText(items.get(position).titleResId);
        viewHolder.iconImageView.setImageResource(items.get(position).iconResId);

        return convertView;
    }

    private static class NavigationDrawerListViewItem {
        private final int titleResId;
        private final int iconResId;
        private final MainActivity.Page page;

        private NavigationDrawerListViewItem(int titleResId, int iconResId, MainActivity.Page page) {
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.page = page;
        }
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public ImageView iconImageView;
    }
}