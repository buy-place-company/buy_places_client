package ru.tp.buy_places;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {
    public static final String PREF_FILE_NAME = "Test";
    private static final String TEXT_FIELD = "text";
    private static final String IMAGE_FIELD = "image";

    Manager mCallBacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private View profileHeader;
    ListView mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private View containerView;

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
        mDrawerListView = (ListView)inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        profileHeader = inflater.inflate(R.layout.header, mDrawerListView, false);
        mDrawerListView.addHeaderView(profileHeader);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        String[] from = new String[] {
            TEXT_FIELD,
            IMAGE_FIELD
        };

        int[] to = new int[] {
                R.id.text,
                R.id.image
        };

        mDrawerListView.setAdapter(new SimpleAdapter(
                getActivity(),
                getData(),
                R.layout.item_navigation,
                from,
                to
        ));
        ImageView image = (ImageView)profileHeader.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_pupkin);

        return mDrawerListView;
    }

    public ArrayList<HashMap<String, Object>> getData(){
        final String TITLES[] = new String[]{
                getString(R.string.maps),
                getString(R.string.myobjects),
                getString(R.string.deals),
                getString(R.string.raiting),
                getString(R.string.settings)};
        final int ICONS[] = {R.mipmap.ic_map,
                R.mipmap.ic_object,
                R.mipmap.ic_deals,
                R.mipmap.ic_raiting,
                R.mipmap.ic_settings};

        ArrayList<HashMap<String, Object>> list =
                new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            HashMap<String, Object> element = new HashMap<>();
            element.put(TEXT_FIELD, TITLES[i]);
            element.put(IMAGE_FIELD, ICONS[i]);
            list.add(element);
        }
        return list;
    }

    private void selectItem(int position) {
        clearBackStack();
        Toast toast = Toast.makeText(getActivity(),
                "", Toast.LENGTH_SHORT);

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(containerView);
        }
            switch (position) {
                case 0:
                    mCallBacks.showUserInfo();
                    toast = Toast.makeText(getActivity(),
                            "Мой кабинет", Toast.LENGTH_SHORT);
                    break;
                case 1:
                    toast = Toast.makeText(getActivity(),
                            "Карта", Toast.LENGTH_SHORT);
                    mCallBacks.showMap();
                    break;
                case 2:
                    toast = Toast.makeText(getActivity(),
                            "Мои объекты", Toast.LENGTH_SHORT);
                    mCallBacks.showMyObjects();
                    break;
                case 3:
                    mCallBacks.showDeals();
                    toast = Toast.makeText(getActivity(),
                            "Мои сделки", Toast.LENGTH_SHORT);
                    break;
                case 4:
                    mCallBacks.showRaiting();
                    toast = Toast.makeText(getActivity(),
                            "Рейтинг", Toast.LENGTH_SHORT);
                    break;
                case 5:
                    toast = Toast.makeText(getActivity(),
                            "Настройки", Toast.LENGTH_SHORT);
                    mCallBacks.showSettings();
                    break;
            }
        toast.show();
        }

    private void clearBackStack() {
        if(getActivity() != null)
            while (getActivity().getFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getFragmentManager().popBackStackImmediate();
    }

    public void setUp(int fragmentId, DrawerLayout layout, Toolbar toolbar){
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = layout;
    }

    public interface Manager  {
        public void showMap();
        public void showMyObjects();
        public void showDeals();
        public void showRaiting();
        public void showSettings();
        public void showUserInfo();
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
