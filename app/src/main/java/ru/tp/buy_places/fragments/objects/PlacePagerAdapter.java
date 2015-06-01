package ru.tp.buy_places.fragments.objects;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.tp.buy_places.fragments.deals.OutboxDealsFragment;

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
                return MyPlacesFragment.newInstance();
            case 2:
                return MyPlacesFragment.newInstance();
            default:
                return MyPlacesFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Мои";
            case 1:
                return "Поблизости";
            case 2:
                return "Посещенные";
            default:
                return "Мои";
        }
    }
}
