package com.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Максим on 11.08.2016.
 */
public class FaragmentMapInfo extends Fragment {
    View v;
    GoogleMap maps;
    MapView mMapView;
    MarkerOptions marker;
    LatLng profilemap;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        profilemap = new LatLng(((ActivityMain) getActivity()).getClient().getLat(), ((ActivityMain) getActivity()).getClient().getLonget());
        maps = mMapView.getMap();
        marker = new MarkerOptions().position(
                profilemap).draggable(false);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        maps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
              geo();
                return false;
            }
        });
        maps.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(profilemap).zoom(16).build();
        maps.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        return v;
    }
    public void geo(){
        String query = "geo:"+marker.getPosition().latitude+","+marker.getPosition().longitude+"?q="+
                marker.getPosition().latitude+","+marker.getPosition().longitude+"("+
                ((ActivityMain) getActivity()).getClient().getProfilename()+" "
                +((ActivityMain) getActivity()).getClient().getLast()+")&z=10";
        Intent intent=new Intent(Intent.ACTION_VIEW,
                Uri.parse(query));
        getActivity().startActivity(intent);
    }
}
