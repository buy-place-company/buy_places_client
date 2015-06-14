package ru.tp.buy_places.fragments.deals;

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
 * Created by Ivan on 10.06.2015.
 */
public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    Context mContext;
    private List<Place> mData = new ArrayList<>();

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public DealsAdapter(Context context){
        mContext = context;
    }

    public void setData(List<Place> data){
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
            mTitle = (TextView) itemView.findViewById(R.id.title_place_list);
            mIcon = (ImageView) itemView.findViewById(R.id.icon_place_list);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(view, getPosition());
            }

        }
    }

    @Override
    public DealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DealsAdapter.ViewHolder holder, int position) {
        if(mData != null) {
            holder.mTitle.setText(mData.get(position).getName()); // Setting the Text with the array of our Titles
            holder.mIcon.setImageResource(R.mipmap.ic_object);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
