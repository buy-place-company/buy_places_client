package ru.tp.buy_places.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import ru.tp.buy_places.R;
import ru.tp.buy_places.fragments.deals.DealsFragment;
import ru.tp.buy_places.fragments.map.MapFragment;
import ru.tp.buy_places.fragments.objects.MyObjectsListFragment;
import ru.tp.buy_places.fragments.raiting.RaitingFragment;
import ru.tp.buy_places.fragments.settings.SettingFragment;
import ru.tp.buy_places.fragments.user.UserFragment;

public class MainActivity extends AppCompatActivity implements
        MyObjectsListFragment.OnFragmentInteractionListener,
        DealsFragment.OnFragmentInteractionListener,
        RaitingFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView)findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);
        showPage(Page.MAP);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.navigation_map:
                showPage(Page.MAP);
                break;
            case R.id.navigation_places:
                showPage(Page.MY_OBJECTS);
                break;
            case R.id.navigation_deals:
                showPage(Page.DEALS);
                break;
            case R.id.navigation_rating:
                showPage(Page.RATING);
                break;
            default:
                showPage(Page.MAP);
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public enum Page {
        MAP,
        MY_OBJECTS,
        DEALS,
        RATING,
        SETTINGS
    }
}
