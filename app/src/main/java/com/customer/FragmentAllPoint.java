package com.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentAllPoint extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private TextView name;
    private TextView lastname;
    private TextView desc;
    private Map<Marker, Client> maps = new HashMap<>();
    ArrayList<Client> arrayList;
    public static final String TAG = "FragmentAllPoint";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maps.clear();
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        setHasOptionsMenu(true);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();

        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        arrayList = db.getmapdata();
        for (int i = 0; i < arrayList.size(); i++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(arrayList.get(i).getLat(), arrayList.get(i).getLonget()))
                    .title(arrayList.get(i).getProfilename())
                    .snippet(arrayList.get(i).getLast())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            ((MainActivity) getActivity()).getClient().setMapid(marker.getId());
        }
//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.TransparentBackground);
//                LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View layout = inflater.inflate(R.layout.marker_view, null);
//                TextView name = (TextView) layout.findViewById(R.id.firsname);
//
////
//                return layout;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }
//        });

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(arrayList.get(arrayList.size()).getLat(), arrayList.get(arrayList.size()).getLonget())).zoom(17).build();
//        googleMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ((MainActivity) getActivity()).getClient().setRecid(recId(marker.getId(),arrayList));
                ((MainActivity) getActivity()).getClient().setCheck(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new FragmentInfo()).commit();

            }
        });
        return v;
    }

    private int recId(String markerid, ArrayList<Client> arrayList) {
        for (Client client : arrayList) {
            if (((MainActivity) getActivity()).getClient().getMapid().equals(markerid)) {
                return client.getRecid();
            }
        }

        return -1;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(false);

    }
}