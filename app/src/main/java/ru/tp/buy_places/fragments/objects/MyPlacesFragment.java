package ru.tp.buy_places.fragments.objects;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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


import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.PlaceActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Places;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPlacesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlacesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MyPlacesAdapter.OnItemClickListener {


    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecycleView;
    private MyPlacesAdapter myPlacesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Places mPlaces;

    public static MyPlacesFragment newInstance(String param1, String param2) {
        MyPlacesFragment fragment = new MyPlacesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MyPlacesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Fragment fragment = new MyPlacesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

//    private void initFragment(){
//        //adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_object, null, new String[]{Places.COLUMN_ALIAS_ID}, new int[]{R.id.title_place_list}, 0);
//        //adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_object, from, to);
//   }


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
        return mRecycleView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        initFragment();
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
        return new CursorLoader(getActivity(), BuyPlacesContract.Places.CONTENT_URI, null, BuyPlacesContract.Places.IS_IN_OWNERSHIP_SELECTION, null, null);
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
        Intent intent = new Intent(getActivity(), PlaceActivity.class);
        intent.putExtra("EXTRA_PLACE_ID", mPlaces.getPlaces().get(position).getRowId());
        startActivity(intent);
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
