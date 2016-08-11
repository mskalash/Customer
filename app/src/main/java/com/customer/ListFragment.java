package com.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Максим on 08.08.2016.
 */
public class ListFragment extends Fragment {
    RecyclerView main;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        main = (RecyclerView) view.findViewById(R.id.main);
        ((MainActivity) getActivity()).getSupportActionBar();
        return view;

    }
}
