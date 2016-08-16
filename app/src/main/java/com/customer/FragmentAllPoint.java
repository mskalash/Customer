package com.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentAllPoint extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
private TextView name;
    private TextView lastname;
    private TextView desc;
    private Map<Marker,DatabaseAdapter> maps=new HashMap<>();
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
        double latitude = 17.385044;
        double longitude = 78.486671;
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude));
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                ContextThemeWrapper wrapper=new ContextThemeWrapper(getActivity(),R.style.TransparentBackground);
                LayoutInflater inflater = (LayoutInflater)  wrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.marker_view, null);
                ImageView image=(ImageView)layout.findViewById(R.id.avatar);
                Glide.with(getActivity())
                        .load(R.drawable.lalal)//<= path of image
                        .bitmapTransform(new CropCircleTransformation(getActivity()))//<= For Circuler image
                        .into(image);
                return layout;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        googleMap.addMarker(marker);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(19).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new FragmentInfo()).commit();

            }
        });
        //   LatLng position = marker.getPosition();

        return v;
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
        menu.getItem(0).setTitle("");


    }
}