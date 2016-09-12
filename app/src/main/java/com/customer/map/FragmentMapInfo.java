package com.customer.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.customer.ActivityMain;
import com.customer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMapInfo extends Fragment {
    private View view;
    private GoogleMap maps;
    private MapView mapView;
    private MarkerOptions marker;
    private LatLng profileMap;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());
        profileMap = new LatLng(((ActivityMain) getActivity()).getClientItem().getLat(), ((ActivityMain) getActivity()).getClientItem().getLonget());
        maps = mapView.getMap();

        marker = new MarkerOptions().position(profileMap).draggable(false);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        maps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                geo();
                return false;
            }
        });
        maps.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(profileMap).zoom(16).build();
        maps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        return view;
    }

    public void geo() {
        String query = "geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "?q=" +
                marker.getPosition().latitude + "," + marker.getPosition().longitude + "(" +
                ((ActivityMain) getActivity()).getClientItem().getProfileName() + " "
                + ((ActivityMain) getActivity()).getClientItem().getLast() + ")&z=10";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        getActivity().startActivity(intent);
    }
}
