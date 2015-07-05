package ru.tp.buy_places.fragments.objects;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.PlaceActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Places;


public class MyPlacesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MyPlacesAdapter.OnItemClickListener {

    private RecyclerView mRecycleView;
    private MyPlacesAdapter myPlacesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Places mPlaces;

    public MyPlacesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Fragment fragment = new MyPlacesFragment();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        getLoaderManager().destroyLoader(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_objects, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.listPlacesRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        myPlacesAdapter = new MyPlacesAdapter(getActivity());
        myPlacesAdapter.setOnItemClickListener(this);
        mRecycleView.setAdapter(myPlacesAdapter);
        getLoaderManager().initLoader(0, null, this);
        return mRecycleView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BuyPlacesContract.Places.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlaces = Places.fromCursor(data);
        myPlacesAdapter.setData(mPlaces.getPlaces());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(View view, int position) {
        final long venuesRowId = myPlacesAdapter.getItemId(position);
        final LatLng venuesLocation = new LatLng(myPlacesAdapter.getItem(position).getLatitude(), myPlacesAdapter.getItem(position).getLongitude());
        final PlaceActivity.VenueType venuesType = mPlaces.getPlaces().get(position).isInOwnership() ? PlaceActivity.VenueType.MINE : mPlaces.getPlaces().get(position).getOwner() == null ? PlaceActivity.VenueType.NOBODYS : PlaceActivity.VenueType.ANOTHERS;
        PlaceActivity.start(this, venuesRowId, venuesLocation, venuesType);
    }

}
