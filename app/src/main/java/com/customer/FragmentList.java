package com.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.customer.map.FragmentAllPoint;
import com.customer.phone.FragmentPhone;
import com.customer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentList extends Fragment implements OnPermissionsListener {
    public final static String TAG = "FragmentList";
    private static RecyclerView main;
    private View view;
    private boolean star = false;
    private ArrayList<ClientItem> arrayList;
    private EditText search;
    public static AHBottomNavigation bottomBar;
    private boolean sortViewName = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        ((ActivityMain) getActivity()).toolbar.setTitle(R.string.app_name);
        setFab();
        recyclerView();
        search = (EditText) view.findViewById(R.id.search);
        addTextListener();
        ((ActivityMain) getActivity()).toolbar.setNavigationIcon(null);
        bottomBar = (AHBottomNavigation) view.findViewById(R.id.bottombar);
        defineBottomBar(bottomBar);

        return view;

    }

    public void recyclerView() {
        main = (RecyclerView) view.findViewById(R.id.main);
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        arrayList = db.getContactsData();
        AdapterList adapter = new AdapterList(getActivity(), arrayList);
        main.setAdapter(adapter);
        main.setLayoutManager(new LinearLayoutManager(getActivity()));
        float bottomDp = getResources().getDimension(R.dimen.bottom_offset_dp);
        Utils.BottomOffsetDecoration bottomOffsetDecoration = new Utils.BottomOffsetDecoration((int) bottomDp);
        main.addItemDecoration(bottomOffsetDecoration);
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
        bottomBar.setCurrentItem(1);

    }

    private void setupBottomBarListener(final AHBottomNavigation bottomBar) {

        bottomBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                boolean sort;
                boolean check = false;
                switch (position) {
                    case 0://Sort Name
                        sort = ((AdapterList) main.getAdapter()).sortName(check);
                        if (sort) {
                            bottomBar.getItem(0).setDrawable(R.drawable.sort_name);
                        } else {
                            bottomBar.getItem(0).setDrawable(R.drawable.revers_name);
                        }

                        sortViewName = true;
                        break;
                    case 1://Sort Date
                        sort = ((AdapterList) main.getAdapter()).sortDate(check, star);
                        if (sort) {
                            bottomBar.getItem(1).setDrawable(R.drawable.sort_date);
                        } else {
                            bottomBar.getItem(1).setDrawable(R.drawable.revers_date);
                        }
                        sortViewName = false;
                        break;
                    case 2://Map
                        if (!Utils.isOnline(getActivity()))
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
                ((ActivityMain) getActivity()).getClientItem().clear();
                dialogContact();
            }
        });
    }
    public void dialogContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.showcont)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 4);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

                    final String text = arrayList.get(i).getProfileName().toLowerCase() + " " + arrayList.get(i).getLast().toLowerCase();
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
                item.setIcon(R.mipmap.favorite);
                ((AdapterList) main.getAdapter()).favorite();
                checkSort();
                bottomBar.restoreBottomNavigation();

                star = true;
                return true;
            } else {
                item.setIcon(R.mipmap.unfavorite);
                ((AdapterList) main.getAdapter()).setArrayList();
                checkSort();
                star = false;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkSort() {
        boolean checkStar = true;

        if (sortViewName) {
            Log.e("Sort", "Name");
            ((AdapterList) main.getAdapter()).sortName(checkStar);
        }
        if (!sortViewName) {
            Log.e("Sort", "Date");
            ((AdapterList) main.getAdapter()).sortDate(checkStar, !star);
        }
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
        db.deleteAll();
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

    @Override
    public void onPermissionsGranted(String[] permission) {
        if (!permission[0].equals(android.Manifest.permission.READ_CONTACTS)) {
            ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
        } else {
            ((ActivityMain) getActivity()).showScreen(new FragmentPhone(), FragmentPhone.TAG, true);
        }
    }



}
