package ru.tp.buy_places.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.tp.buy_places.NavigationDrawerFragment;
import ru.tp.buy_places.R;
import ru.tp.buy_places.fragments.deals.DealsFragment;
import ru.tp.buy_places.fragments.map.MapFragment;
import ru.tp.buy_places.fragments.objects.MyObjectsListFragment;
import ru.tp.buy_places.fragments.raiting.RaitingFragment;
import ru.tp.buy_places.fragments.settings.SettingFragment;
import ru.tp.buy_places.fragments.user.UserFragment;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.Manager,
        MyObjectsListFragment.OnFragmentInteractionListener,
        DealsFragment.OnFragmentInteractionListener,
        RaitingFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private View mDrawerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerView = findViewById(R.id.navigation);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showPage(Page.MAP);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
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



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onNavigationDrawerItemClick(final Page page) {
        mDrawerLayout.closeDrawer(mDrawerView);
        showPage(page);
    }

    private void showPage(Page page) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(page.name());
        if (fragment == null) {
            switch (page) {
                case MAP:
                    fragment = new MapFragment();
                    break;
                case MY_OBJECTS:
                    fragment = new MyObjectsListFragment();
                    break;
                case DEALS:
                    fragment = new DealsFragment();
                    break;
                case RATING:
                    fragment = new RaitingFragment();
                    break;
                case SETTINGS:
                    fragment = new SettingFragment();
                    break;
                default:
                    fragment = new MapFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment, page.name()).commit();
        }

    }

    public enum Page {
        MAP,
        MY_OBJECTS,
        DEALS,
        RATING,
        SETTINGS
    }
}
