package com.customer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Максим on 02.08.2016.
 */
public class FragmentMap extends Fragment implements OnPermissionsListener {

    MapView mMapView;
    MarkerOptions marker;
    private GoogleMap googleMap;
    double longit;
    double lat;
    LatLng position;
    boolean camera = true;
    public final static String TAG = "FragmentMap";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);
        if ((((ActivityMain) getActivity()).getClient().getLat() != 0) && (((ActivityMain) getActivity()).getClient().getLonget() != 0)) {
            setmarker();
            camera = false;
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {


            @Override
            public void onMyLocationChange(Location location) {
                if (camera) {
                    longit = location.getLongitude();
                    lat = location.getLatitude();

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(lat, longit)).zoom(17).build();
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    camera = false;
                    return;
                }
                return;
            }
        });

        googleMap
                .setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();
                        marker = new MarkerOptions().position(
                                latLng);
                        marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        googleMap.addMarker(marker);
                        position = marker.getPosition();
                    }
                });


        return v;
    }

    public void setmarker() {
        LatLng markerposition = new LatLng(((ActivityMain) getActivity()).getClient().getLat(), ((ActivityMain) getActivity()).getClient().getLonget());
        marker = new MarkerOptions().position(
                markerposition);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerposition).zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
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
        menu.getItem(0).setVisible(true);
        menu.getItem(0).setTitle("NEXT");
        menu.getItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            if (position != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.showcont)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ((ActivityMain) getActivity()).showScreen(new FragmentPhone(), FragmentPhone.TAG, true);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                ((ActivityMain) getActivity()).getClient().setLat(position.latitude);
                ((ActivityMain) getActivity()).getClient().setLonget(position.longitude);
            } else if ((((ActivityMain) getActivity()).getClient().getLat() != 0) && (((ActivityMain) getActivity()).getClient().getLonget() != 0)) {
                ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
            } else Toast.makeText(getActivity(), R.string.selectpoint, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
    }
}