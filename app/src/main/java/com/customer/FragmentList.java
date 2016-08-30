package com.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentList extends Fragment implements OnPermissionsListener {
    RecyclerView main;
    View view;

    public final static String TAG = "FragmentList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline())
                    Toast.makeText(getActivity(),  R.string.noinet, Toast.LENGTH_LONG).show();
                ((ActivityMain) getActivity()).getClient().clear();
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });
        main = (RecyclerView) view.findViewById(R.id.main);
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        ArrayList<Client> arrayList = db.getContactsData();
        AdapterList adapter = new AdapterList(getActivity(), arrayList);
        main.setAdapter(adapter);
        main.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((ActivityMain) getActivity()).mToolbar.setNavigationIcon(null);
        return view;

    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        ((ActivityMain) getActivity()).showScreen(new FragmentMap(), FragmentMap.TAG, true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setIcon(R.mipmap.map);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(true);
        menu.getItem(0).setTitle(R.string.del).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all) {
            if (!isOnline())
                Toast.makeText(getActivity(), R.string.noinet, Toast.LENGTH_LONG).show();
            ((ActivityMain) getActivity()).showScreen(new FragmentAllPoint(), FragmentAllPoint.TAG, true);

            return true;
        }
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.deleteall)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delete_all();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        if (id == R.id.favorite) {
            if (!star) {
                ((AdapterList) main.getAdapter()).favorite();
                star = true;

                return true;

            } else {
                ((AdapterList) main.getAdapter()).unfavorite();
                star = false;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean star = false;

    public void delete_all() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        db.deleteall();
        ((AdapterList) main.getAdapter()).delete();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name));
        if (file.exists()) {
            String deleteCmd = "rm -r " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name);
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }

    }

}
