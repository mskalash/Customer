package com.customer;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentFirst extends Fragment implements OnPermissionsListener {
    View view;
    FancyButton list;
    FancyButton add;
    boolean tool = false;
    public final static String TAG = "FragmentFirst";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getClient().clear();
        list = (FancyButton) view.findViewById(R.id.list);
        add = (FancyButton) view.findViewById(R.id.newperson);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).showScreen(new FragmentList(), FragmentList.TAG, true);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        });


            ((MainActivity) getActivity()).mToolbar.setNavigationIcon(null);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(false);

    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        ((MainActivity) getActivity()).showScreen(new FragmentMap(), FragmentMap.TAG, true);
    }
}