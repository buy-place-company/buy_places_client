package ru.tp.buy_places.fragments.objects;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import ru.tp.buy_places.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyObjectsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyObjectsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyObjectsListFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private SimpleAdapter adapter;
    private static final String TEXT_FIELD = "text";
    private static final String IMAGE_FIELD = "image";


    public static MyObjectsListFragment newInstance(String param1, String param2) {
        MyObjectsListFragment fragment = new MyObjectsListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MyObjectsListFragment() {
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
                R.id.textObj,
                R.id.imageObj
        };

        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_object, from, to);

    }

    private ArrayList<HashMap<String, Object>> getData(){
        final String TITLES[] = new String[]{
                getString(R.string.mgtu),
                getString(R.string.yaposha),
                getString(R.string.spar),
                getString(R.string.mc),
                getString(R.string.obsh)};
        final int IMAGE = R.mipmap.ic_object;
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
        mListView = (ListView) inflater.inflate(R.layout.fragment_my_objects, container, false);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ObjectFragment objectFragment = new ObjectFragment();
                getFragmentManager().
                        beginTransaction().
                        replace(R.id.content,objectFragment).
                        commit();
            }
        });
        return mListView;
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
