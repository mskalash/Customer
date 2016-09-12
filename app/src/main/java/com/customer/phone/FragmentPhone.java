package com.customer.phone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.customer.ActivityMain;
import com.customer.FragmentNew;
import com.customer.R;

public class FragmentPhone extends Fragment {
    public final static String TAG = "FragmentPhone";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_phone, container, false);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.phonefeagment);
        AdapterPhone adapterPhone = new AdapterPhone(getActivity());
        recyclerView.setAdapter(adapterPhone);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(1).setVisible(false);
        menu.getItem(0).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSettings) {
            ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
        }
        return true;
    }
}
