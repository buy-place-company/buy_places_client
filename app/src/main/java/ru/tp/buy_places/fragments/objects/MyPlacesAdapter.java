package ru.tp.buy_places.fragments.objects;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.resourses.Place;

/**
 * Created by home on 21.05.2015.
 */
public class MyPlacesAdapter extends RecyclerView.Adapter<MyPlacesAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    Activity activity;
    private List<Place> mData = new ArrayList<>();

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    MyPlacesAdapter(Context context){
        activity = (Activity)context;
    }

    public void setData(List<Place> data){
        mData.clear();
        if(data != null)
          mData.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mLevel;
        public ImageView mIcon;
        public TextView mCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.text_view_rating);
            mLevel = (TextView) itemView.findViewById(R.id.tv_level);
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_rating);
            mCategory = (TextView) itemView.findViewById(R.id.tv_category);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(view, getPosition());
            }

        }
    }

    @Override
    public MyPlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object_my, parent, false);
        return new ViewHolder(v);
     }

    @Override
    public void onBindViewHolder(MyPlacesAdapter.ViewHolder holder, int position) {
        if(mData != null && mData.get(position).isInOwnership()) {
            holder.mTitle.setText(mData.get(position).getName()); // Setting the Text with the array of our Titles
            holder.mIcon.setImageResource(R.mipmap.ic_object);
            holder.mLevel.setText(Integer.toString(mData.get(position).getLevel()));
            holder.mCategory.setText(mData.get(position).getCategory());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getRowId();
    }

    public Place getItem(int position) {
        return mData.get(position);
    }
}
