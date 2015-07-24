package ru.tp.buy_places.fragments.objects;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Ivan on 07.04.2015.
 */
public class PlacePagerAdapter extends FragmentPagerAdapter {

    public PlacePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return MyPlacesFragment.newInstance();
            case 1:
                return NearPlacesFragment.newInstance();
            case 2:
                return VisitedPlacesFragment.newInstance();
            default:
                return MyPlacesFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Мои";
            case 1:
                return "Поблизости";
            case 2:
                return "Избранные";
            default:
                return "Мои";
        }
    }
}
