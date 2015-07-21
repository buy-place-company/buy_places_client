package ru.tp.buy_places.fragments.raiting;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.UserActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Player;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RaitingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RaitingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaitingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RatingAdapter.OnItemClickListener {

    List<Player> mPlayers;
    private RecyclerView mRecycleView;
    private RatingAdapter ratingAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String TEXT_FIELD = "text";
    private static final String IMAGE_FIELD = "image";

    private OnFragmentInteractionListener mListener;

    public static RaitingFragment newInstance(String param1, String param2) {
        RaitingFragment fragment = new RaitingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public RaitingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        ServiceHelper.get(getActivity()).getRating(20, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_raiting, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.ratingRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        ratingAdapter = new RatingAdapter(getActivity());
        ratingAdapter.setOnItemClickListener(this);
        mRecycleView.setAdapter(ratingAdapter);
        return mRecycleView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BuyPlacesContract.Players.CONTENT_URI, BuyPlacesContract.Players.ALL_COLUMNS_PROJECTION, BuyPlacesContract.Players.WITH_SPECIFIED_RATING_POSITION, null, BuyPlacesContract.Players.COLUMN_POSITION);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlayers = new ArrayList<>();
        while (data.moveToNext()) {
            mPlayers.add(Player.fromCursor(data));
        }
        ratingAdapter.setData(mPlayers);
        Log.d("PLAYERS SIZE", Integer.toString(mPlayers.size()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("EXTRA_USER_ID", mPlayers.get(position).getRowId());
        startActivity(intent);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
