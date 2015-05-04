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

public class OutboxDealsFragment extends Fragment {

    private ListView mListView;
    private SimpleAdapter adapter;
    private static final String TEXT_FIELD = "text";
    private static final String IMAGE_FIELD = "image";


    private void initFragment(){
        String from[] = new String[]{
                TEXT_FIELD,
                IMAGE_FIELD
        };

        int[] to = new int[] {
                R.id.textObj,
                R.id.imageObj
        };

        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_inbox, from, to);
    }

    private ArrayList<HashMap<String, Object>> getData(){
        final String TITLE =  getString(R.string.mgtu);

        final int IMAGE = R.mipmap.ic_object;
        ArrayList<HashMap<String, Object>> list =
                new ArrayList<>();
        HashMap<String, Object> element = new HashMap<>();
        element.put(TEXT_FIELD, TITLE);
        element.put(IMAGE_FIELD, IMAGE);
        list.add(element);
        return list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initFragment();
    }

    public static OutboxDealsFragment newInstance() {
        OutboxDealsFragment fragment = new OutboxDealsFragment();
        return fragment;
    }

    public OutboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_inbox_deals, container, false);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ObjectActivity.class);
                startActivity(intent);
            }
        });

        return mListView;
    }

}
