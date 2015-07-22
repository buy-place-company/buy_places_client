package ru.tp.buy_places.fragments.objects;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Places;
import ru.tp.buy_places.utils.AccountManagerHelper;


public class MyPlacesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_USER_ID = "ARG_USER_ID";
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

    public static Fragment newInstance(long userId) {
        Fragment fragment = new MyPlacesFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        mRecycleView.setAdapter(myPlacesAdapter);
        long userId = getArguments()!=null && getArguments().containsKey(ARG_USER_ID)?getArguments().getLong(ARG_USER_ID):AccountManagerHelper.getPlayerId(getActivity());
        final Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        getLoaderManager().initLoader(0, args, this);
        return mRecycleView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long userId = args.getLong(ARG_USER_ID);
        return new CursorLoader(getActivity(), BuyPlacesContract.Places.CONTENT_URI, BuyPlacesContract.Places.ALL_COLUMNS_PROJECTION, BuyPlacesContract.Places.WITH_SPECIFIED_OWNER_ID_SELECTION, new String[]{String.valueOf(userId)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlaces = Places.fromCursor(data);
        myPlacesAdapter.setData(mPlaces.getPlaces());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myPlacesAdapter.setData(null);
    }

}
