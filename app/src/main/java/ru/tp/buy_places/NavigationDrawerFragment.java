package ru.tp.buy_places;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import ru.tp.buy_places.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String PREF_FILE_NAME = "Test";

    Manager mCallBacks;
    private View mProfileHeader;
    private ListView mDrawerListView;
    private NavigationDrawerListViewAdapter mNavigationDrawerListViewAdapter;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(PREF_FILE_NAME, "OnCreateView");
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mProfileHeader = inflater.inflate(R.layout.header, mDrawerListView, false);
        mDrawerListView.addHeaderView(mProfileHeader);
        mDrawerListView.setOnItemClickListener(this);
        mNavigationDrawerListViewAdapter = new NavigationDrawerListViewAdapter(getActivity());
        mDrawerListView.setAdapter(mNavigationDrawerListViewAdapter);

        ImageView image = (ImageView) mProfileHeader.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_pupkin);
        return mDrawerListView;
    }

    private void clearBackStack() {
        if (getActivity() != null)
            while (getActivity().getFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getFragmentManager().popBackStackImmediate();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallBacks.onNavigationDrawerItemClick(MainActivity.Page.values()[(int) mDrawerListView.getItemIdAtPosition(position)]);
    }

    public interface Manager {
        void onNavigationDrawerItemClick(MainActivity.Page page);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBacks = (Manager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Manager.");
        }
    }
}
