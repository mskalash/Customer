package com.customer.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.customer.ActivityMain;
import com.customer.ClientItem;
import com.customer.DatabaseAdapter;
import com.customer.FragmentInfo;
import com.customer.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentAllPoint extends Fragment {
    public static final String TAG = "FragmentAllPoint";
    MapView mapView;

    private GoogleMap googleMap;
    private Map<Marker, ClientItem> maps = new HashMap<>();
    ArrayList<ClientItem> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maps.clear();
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        ((ActivityMain) getActivity()).toolbar.setTitle(R.string.app_name);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        googleMap = mapView.getMap();
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        arrayList = db.getmapdata();
        setMarker();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ((ActivityMain) getActivity()).getClientItem().setRecid(recId(marker.getId(), arrayList));
                ((ActivityMain) getActivity()).getClientItem().setCheck(true);
                ((ActivityMain) getActivity()).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);

            }
        });
        return v;
    }

    public void setMarker() {
        for (int i = 0; i < arrayList.size(); i++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(arrayList.get(i).getLat(), arrayList.get(i).getLonget()))
                    .title(arrayList.get(i).getProfileName())
                    .snippet(arrayList.get(i).getLast())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            arrayList.get(i).setMapid(marker.getId());
        }
    }

    private int recId(String markerId, ArrayList<ClientItem> arrayList) {
        for (ClientItem clientItem : arrayList) {
            if (clientItem.getMapid().equals(markerId)) {
                return clientItem.getId();
            }
        }
        return -1;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);

    }
}