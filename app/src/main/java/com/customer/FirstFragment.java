package com.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Максим on 08.08.2016.
 */
public class FirstFragment extends Fragment {
    View view;
    FancyButton list;
    FancyButton add;
    TextView left;
    TextView right;
    TextView center;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        list= (FancyButton)view.findViewById(R.id.list);
        add= (FancyButton)view.findViewById(R.id.newperson);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ListFragment()).commit();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new FragmentMap()).commit();
            }
        });
        return view;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle(" ");

    }
}