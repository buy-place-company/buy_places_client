package ru.tp.buy_places.fragments.raiting;

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
import ru.tp.buy_places.service.resourses.Player;

/**
 * Created by home on 21.05.2015.
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private LayoutInflater inflater;
    OnItemClickListener mItemClickListener;
    Activity activity;
    private List<Player> mData = new ArrayList<>();

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    RatingAdapter(Context context){
        inflater = LayoutInflater.from(context);
        activity = (Activity)context;
    }

    public void setData(List<Player> data){
        mData.clear();
        if(data != null)
          mData.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.text_view_rating);
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_rating);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(view, getPosition());
            }

        }
    }

    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raiting, parent, false);
        return new ViewHolder(v);
     }

    @Override
    public void onBindViewHolder(RatingAdapter.ViewHolder holder, int position) {
        if(mData != null) {
            holder.mTitle.setText(mData.get(position).getUsername()); // Setting the Text with the array of our Titles
            holder.mIcon.setImageResource(R.mipmap.ic_object);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}