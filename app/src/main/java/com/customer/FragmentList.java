package com.customer;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentList extends Fragment implements OnPermissionsListener{
    RecyclerView main;
    View view;
    public final static String TAG = "FragmentList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        FloatingActionButton fab= (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        });
        main = (RecyclerView) view.findViewById(R.id.main);
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        ArrayList<Client> arrayList = db.getContactsData();
        ListAdapter adapter = new ListAdapter(getActivity(),arrayList);
        main.setAdapter(adapter);
        main.setLayoutManager(new GridLayoutManager(getContext(), 3));
        ((MainActivity) getActivity()).mToolbar.setNavigationIcon(null);
        return view;

    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        ((MainActivity) getActivity()).showScreen(new FragmentMap(), FragmentMap.TAG, true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(true);
        menu.getItem(0).setTitle("MAP");
        menu.getItem(1).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            ((MainActivity) getActivity()).showScreen(new FragmentAllPoint(), FragmentAllPoint.TAG, true);

            return true;
        }
        if(id==R.id.delete_all){
            delete_all();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void delete_all(){
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        db.deleteall();
        ((ListAdapter) main.getAdapter()).delete();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name));
        if (file.exists()) {
            String deleteCmd = "rm -r " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name);
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }

    }
}
