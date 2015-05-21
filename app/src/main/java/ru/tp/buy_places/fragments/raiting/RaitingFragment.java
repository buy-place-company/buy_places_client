package ru.tp.buy_places.fragments.raiting;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Player;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RaitingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RaitingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaitingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private ListView mListView;
    private SimpleAdapter adapter;
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
    }

    private void initFragment(){
        String from[] = new String[]{
                TEXT_FIELD,
                IMAGE_FIELD
        };

        int[] to = new int[] {
                R.id.text_view_rating,
                R.id.image_view_rating
        };

        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_raiting, from, to);
        getLoaderManager().initLoader(0, null, this);
    }

    private ArrayList<HashMap<String, Object>> getData(){
        final String TITLES[] = new String[]{
                getString(R.string.petya),
                getString(R.string.vasya),
                getString(R.string.gosha)};
        final int IMAGE = R.mipmap.ic_pupkin;
        ArrayList<HashMap<String, Object>> list =
                new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            HashMap<String, Object> element = new HashMap<>();
            element.put(TEXT_FIELD, TITLES[i]);
            element.put(IMAGE_FIELD, IMAGE);
            list.add(element);
        }
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mListView = (ListView) inflater.inflate(R.layout.fragment_raiting, container, false);
        return mListView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initFragment();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BuyPlacesContract.Players.CONTENT_URI, BuyPlacesContract.Players.ALL_COLUMNS_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Player> players = new ArrayList<>();
        while (data.moveToNext()) {
            players.add(Player.fromCursor(data));
        }
        Log.d("PLAYERS SIZE", Integer.toString(players.size()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
