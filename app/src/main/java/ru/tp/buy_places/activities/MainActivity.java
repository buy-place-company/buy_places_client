package ru.tp.buy_places.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ru.tp.buy_places.NavigationDrawerFragment;
import ru.tp.buy_places.R;
import ru.tp.buy_places.fragments.myobjects.MyObjectsFragment;

public class MainActivity extends ActionBarActivity implements Manager{

    public static final String MAP_FRAGMENT_TAG = "map";
    public static final String MY_OBJECTS_FRAGMENT_TAG = "objects";
    public static final String DEALS_FRAGMENT_TAG = "deals";
    public static final String RAITING_FRAGMENT_TAG = "raiting";
    public static final String SETTINGS_FRAGMENT_TAG = "settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment navDrawer = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation);
        navDrawer.setUp(R.id.navigation, (DrawerLayout)findViewById(R.id.drawer), toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFragment(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        Log.d(MY_OBJECTS_FRAGMENT_TAG, "setFragment");
        Fragment fragment = manager.findFragmentByTag(tag);
        if(fragment == null) {
            switch(tag) {
                case MAP_FRAGMENT_TAG:

                    break;
                case MY_OBJECTS_FRAGMENT_TAG:
                    fragment = new MyObjectsFragment();
                    break;
                case DEALS_FRAGMENT_TAG:

                    break;
                case RAITING_FRAGMENT_TAG:

                    break;
                case SETTINGS_FRAGMENT_TAG:

                    break;

            }
            manager.beginTransaction()
                    .replace(R.id.content, fragment, tag)
                    .commit();
        }
    }

    @Override
    public void showMyObjects() {
        setFragment(MY_OBJECTS_FRAGMENT_TAG);
    }
}
