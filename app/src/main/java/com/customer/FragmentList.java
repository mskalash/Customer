package com.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.customer.map.FragmentAllPoint;
import com.customer.map.FragmentMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentList extends Fragment implements OnPermissionsListener {
    public final static String TAG = "FragmentList";
    public RecyclerView main;
    public View view;
    public boolean star = false;
    public ArrayList<ClientItem> arrayList;
    public EditText search;
    public AHBottomNavigation bottomBar;
    public boolean sortViewName = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        ((ActivityMain) getActivity()).toolbar.setTitle(R.string.app_name);
        setFab();
        main = (RecyclerView) view.findViewById(R.id.main);
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        arrayList = db.getContactsData();
        AdapterList adapter = new AdapterList(getActivity(), arrayList);
        main.setAdapter(adapter);
        main.setLayoutManager(new LinearLayoutManager(getActivity()));
        search = (EditText) view.findViewById(R.id.search);
        addTextListener();
        ((ActivityMain) getActivity()).toolbar.setNavigationIcon(null);
        bottomBar = (AHBottomNavigation) view.findViewById(R.id.bottombar);
        defineBottomBar(bottomBar);


        return view;

    }

    private void defineBottomBar(AHBottomNavigation bottomBar) {
        AHBottomNavigationItem[] navigationItems = defineNavigationItems();
        setupNavigationItems(navigationItems);
        setupBottomBarParams(bottomBar);
        setupBottomBarListener(bottomBar);
    }

    private void setupNavigationItems(AHBottomNavigationItem[] navigationItems) {
        bottomBar.addItems(Arrays.asList(navigationItems));
    }


    private AHBottomNavigationItem[] defineNavigationItems() {
        return new AHBottomNavigationItem[]{
                new AHBottomNavigationItem("Sort Name", R.drawable.list_icon, R.color.colorPrimaryDark),
                new AHBottomNavigationItem("Sort Date", R.drawable.sort_date, R.color.colorPrimaryDark),
                new AHBottomNavigationItem("Maps", R.mipmap.map, R.color.colorPrimaryDark),
        };
    }

    private void setupBottomBarParams(AHBottomNavigation bottomBar) {
        bottomBar.setDefaultBackgroundColor(Color.parseColor("#303F9F"));
        bottomBar.setBehaviorTranslationEnabled(true);
        bottomBar.setAccentColor(Color.parseColor("#ffffff"));
        bottomBar.setInactiveColor(Color.parseColor("#ffffff"));
        bottomBar.setForceTint(true);
        bottomBar.setCurrentItem(0);
    }

    private void setupBottomBarListener(final AHBottomNavigation bottomBar) {

        bottomBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                boolean sort;


                switch (position) {
                    case 0://Sort Name

                        sort = ((AdapterList) main.getAdapter()).sortName();
                        if (sort) {
                            bottomBar.getItem(0).setDrawable(R.drawable.sort_name);
                        } else {
                            bottomBar.getItem(0).setDrawable(R.drawable.revers_name);
                        }

                        sortViewName = true;
                        break;
                    case 1://Sort Date
                        sort = ((AdapterList) main.getAdapter()).sortDate();
                        if (sort) {
                            bottomBar.getItem(1).setDrawable(R.drawable.sort_date);
                        } else {
                            bottomBar.getItem(1).setDrawable(R.drawable.revers_date);
                        }
                        sortViewName = false;
                        break;
                    case 2://Map
                        if (!isOnline())
                            Toast.makeText(getActivity(), R.string.noinet, Toast.LENGTH_LONG).show();
                        ((ActivityMain) getActivity()).showScreen(new FragmentAllPoint(), FragmentAllPoint.TAG, true);
                        break;
                }
                setIconBottomBar();
                bottomBar.refresh();

            }

        });
    }

    public void setIconBottomBar() {
        if (sortViewName) {
            bottomBar.getItem(1).setDrawable(R.drawable.list_icon);
        } else {
            bottomBar.getItem(0).setDrawable(R.drawable.list_icon);
        }
    }

    public void setFab() {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline())
                    Toast.makeText(getActivity(), R.string.noinet, Toast.LENGTH_LONG).show();
                ((ActivityMain) getActivity()).getClientItem().clear();
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(true);
        menu.getItem(0).setTitle(R.string.del).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSettings) {
            showDialog();
            return true;
        }
        if (id == R.id.deleteAll) {
            if (!star) {

                ((AdapterList) main.getAdapter()).favorite();
                star = true;
                return true;
            } else {
                ((AdapterList) main.getAdapter()).setArrayList();
                if (((AdapterList) main.getAdapter()).nameSort) {
                    ((AdapterList) main.getAdapter()).sortName();
                }
                if (((AdapterList) main.getAdapter()).dateSort) {
                    ((AdapterList) main.getAdapter()).sortDate();
                }
                star = false;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.deleteall)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAll();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void deleteAll() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        db.deleteall();
        ((AdapterList) main.getAdapter()).deleteAll();
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

    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();
                final ArrayList<ClientItem> filteredList = new ArrayList<>();

                for (int i = 0; i < arrayList.size(); i++) {

                    final String text = arrayList.get(i).getProfileName().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(arrayList.get(i));
                    }
                }
                AdapterList adapter = new AdapterList(getActivity(), filteredList);
                main.setAdapter(adapter);
                main.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onPermissionsGranted(String[] permission) {
        ((ActivityMain) getActivity()).showScreen(new FragmentMap(), FragmentMap.TAG, true);
    }


}
