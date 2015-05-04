package ru.tp.buy_places.fragments.deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.ObjectActivity;

public class InboxDealsFragment extends Fragment {

    private ListView mListView;
    private SimpleAdapter adapter;
    private static final String TEXT_FIELD = "text";
    private static final String IMAGE_FIELD = "image";
    public static final String DIALOG = "Сделка";
    AlertDialog.Builder ad;

    private void initFragment(){
        String from[] = new String[]{
                TEXT_FIELD,
                IMAGE_FIELD
        };

        int[] to = new int[] {
                R.id.text_view_deals,
                R.id.image_view_deals
        };

        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_inbox, from, to);
    }

    private ArrayList<HashMap<String, Object>> getData(){
        final String TITLES[] = new String[]{
                getString(R.string.deals1),
                getString(R.string.deals2)
        };
        final int IMAGE = R.mipmap.ic_object;
        ArrayList<HashMap<String, Object>> list =
                new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            HashMap<String, Object> element = new HashMap<>();
            element.put(TEXT_FIELD, TITLES[i]);
            element.put(IMAGE_FIELD, IMAGE);
            list.add(element);
        }
        return list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initFragment();
    }

    public static InboxDealsFragment newInstance() {
        InboxDealsFragment fragment = new InboxDealsFragment();
        return fragment;
    }

    public InboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_inbox_deals, container, false);
        mListView.setAdapter(adapter);
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
