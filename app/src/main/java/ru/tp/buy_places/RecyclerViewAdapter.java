package ru.tp.buy_places;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


/**
 * Created by home on 15.03.2015.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    private LayoutInflater inflater;
    private static final int TYPE_ITEM = 1;

    List<NavigationDrawerItem> data = Collections.emptyList();
    String name;
    int icon;
    private static final String TAG = "Deb";


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        int Holderid;
        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            if(ViewType == TYPE_ITEM) {
                title = (TextView) itemView.findViewById(R.id.rowText);
                icon = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else {
                title = (TextView) itemView.findViewById(R.id.name);
                icon = (ImageView) itemView.findViewById(R.id.circleView);
            }
        }
    }

    RecyclerViewAdapter(Context context, List<NavigationDrawerItem> data, String name, int icon){ // RecyclerViewAdapter Constructor with titles and icons parameter
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.name = name;
        this.icon = icon;
    }

    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            Log.d(TAG, "item");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            Log.d(TAG, "head");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            ViewHolder vhHeader = new ViewHolder(v,viewType);
            return vhHeader;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.title.setText(data.get(position-1).getTitle()); // Setting the Text with the array of our Titles
            holder.icon.setImageResource(data.get(position-1).getIcon());// Settimg the image with array of our icons
        }
        else{
            holder.icon.setImageResource(icon);           // Similarly we set the resources for header view
            holder.title.setText(name);
        }
    }

    // This method returns the number of items present in the list

    public int getItemCount() {
        return data.size()+1;// the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
