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
public class FragmentList extends Fragment {
    RecyclerView main;
    View view;
    public final static String TAG = "FragmentList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        main = (RecyclerView) view.findViewById(R.id.main);
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        ArrayList<Client> arrayList = db.getContactsData();
        ListAdapter adapter = new ListAdapter(getActivity(),arrayList);
        main.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main.setLayoutManager(layoutManager);
        return view;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(true);
        menu.getItem(0).setTitle("MAP");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            ((MainActivity) getActivity()).showScreen(new FragmentAllPoint(), FragmentAllPoint.TAG, true);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
