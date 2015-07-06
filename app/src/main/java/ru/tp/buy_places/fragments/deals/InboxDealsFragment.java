package ru.tp.buy_places.fragments.deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ru.tp.buy_places.R;

public class InboxDealsFragment extends Fragment {

    private ListView mListView;
    public static final String DIALOG = "Сделка";
    AlertDialog.Builder ad;

    public InboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_inbox_deals, container, false);
        ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(DIALOG);
        ad.setMessage("Осуществить сделку?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getActivity(), "Сделка прошла удачно",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getActivity(), "Сделка не состоялась",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getActivity(), "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ad.show();
            }
        });

        return mListView;
    }
}
