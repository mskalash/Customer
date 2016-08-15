package com.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Максим on 08.08.2016.
 */
public class ListFragment extends Fragment {
    RecyclerView main;
    View view;
    public final static String TAG = "ListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        main = (RecyclerView) view.findViewById(R.id.main);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        ArrayList<Client> arrayList = db.getContactsData();
        ListAdapter adapter = new ListAdapter(arrayList);
        main.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main.setLayoutManager(layoutManager);
        // getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        return view;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle("MAP");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new AllPointFragment()).commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
