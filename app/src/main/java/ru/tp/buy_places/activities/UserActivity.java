package ru.tp.buy_places.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.fragments.objects.MyPlacesFragment;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Player;

public class UserActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor> {

    private Player mPlayer;
    private TextView userName;
    private TextView userVenueCount;
    private TextView userVenueMax;
    private TextView userCash;
    private TextView userLevel;
    private ImageView avatar;

    private RecyclerView mInfoList;
    Fragment fragment;
//    private VenueListAdapter mVenueListAdapter;
//    OnItemClickListener mItemClickListener;
    private static final int USER_LOADER_ID = 0;
    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        avatar = (ImageView) findViewById(R.id.image_view_user);
        userName = (TextView)findViewById(R.id.text_view_user_name);
        userVenueCount = (TextView)findViewById(R.id.tv_object_count);
        userVenueMax = (TextView)findViewById(R.id.tv_maxobject);
        userCash = (TextView)findViewById(R.id.tv_score);
        userLevel = (TextView)findViewById(R.id.tv_level);
        Intent intent = getIntent();
        final long userId = intent.getLongExtra(EXTRA_USER_ID, -1);
        Bundle args = new Bundle();
        args.putLong(EXTRA_USER_ID, userId);
        getSupportLoaderManager().initLoader(USER_LOADER_ID, args, this);
        fragment = MyPlacesFragment.newInstance(userId);
        getFragmentManager().beginTransaction().replace(R.id.content_venues, fragment).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           if (toolbar != null)
              setSupportActionBar(toolbar);
        ServiceHelper.get(this).getPlayerVenues(userId);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final long userId = args.getLong(EXTRA_USER_ID);
        return new CursorLoader(this, BuyPlacesContract.Players.CONTENT_URI, null, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{String.valueOf(userId)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        fragment.getView().setBackgroundColor(Color.TRANSPARENT);
        if (data.moveToFirst()) {
            mPlayer = Player.fromCursor(data);
            if(mPlayer.getUsername() != null){
                userName.setText(mPlayer.getUsername());
                userVenueCount.setText(Integer.toString(mPlayer.getPlaces()));
                userVenueMax.setText(Integer.toString(mPlayer.getMaxPlaces()));
                userLevel.setText(Integer.toString(mPlayer.getLevel()));
                userCash.setText(Long.toString(mPlayer.getCash()));
                //avatar.setBackgroundResource(R.drawable.circle_background);

            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static void start(Context context, Player player) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER_ID, player.getId());
        context.startActivity(intent);
    }

}
