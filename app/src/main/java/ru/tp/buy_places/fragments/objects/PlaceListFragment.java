package ru.tp.buy_places.fragments.objects;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ru.tp.buy_places.fragments.objects.PlaceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ru.tp.buy_places.fragments.objects.PlaceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceListFragment extends Fragment {


    private PlacePagerAdapter mPlacePagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private OnFragmentInteractionListener mListener;
    private AppBarLayout mAppBarLayout;

    public static PlaceListFragment newInstance() {
        PlaceListFragment fragment = new PlaceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placelist, container, false);
        //mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        //mTabLayout = new TabLayout(getActivity());
        //TabLayout.LayoutParams params = new TabLayout.LayoutParams(TabLayout.LayoutParams.MATCH_PARENT, TabLayout.LayoutParams.WRAP_CONTENT);
        //mAppBarLayout.addView(mTabLayout, params);
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        //mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mPlacePagerAdapter = new PlacePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPlacePagerAdapter);
        //mTabLayout.setTabsFromPagerAdapter(mPlacePagerAdapter);
        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mAppBarLayout.removeView(mTabLayout);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
