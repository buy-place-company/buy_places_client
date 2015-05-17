package ru.tp.buy_places.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import ru.tp.buy_places.R;

public class PlaceActivity extends ActionBarActivity implements OnClickListener
{

    private Button upgrade;
    private Button sell;

    public static final String DIALOG = "Сделка";
   private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ad = new AlertDialog.Builder(this);
        ad.setTitle(DIALOG);

        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });

        upgrade = (Button)findViewById(R.id.button_upgrade);
        sell = (Button)findViewById(R.id.button_sell);
        OnClickListener upgradeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage("Вы можете улучшить здание за 500р");
                ad.show();
            }
        };
        OnClickListener sellListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage("Вы можете продать здание за 50% стоимости");
                ad.show();
            }
        };

        upgrade.setOnClickListener(upgradeListener);
        sell.setOnClickListener(sellListener);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_object, menu);

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
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

    }
}
