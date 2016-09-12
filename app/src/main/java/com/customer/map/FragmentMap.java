package com.customer.map;

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

import com.customer.ActivityMain;
import com.customer.FragmentNew;
import com.customer.phone.FragmentPhone;
import com.customer.OnPermissionsListener;
import com.customer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends Fragment implements OnPermissionsListener, GoogleMap.OnMyLocationChangeListener {

    public final static String TAG = "FragmentMap";

    private MapView mapView;
    private MarkerOptions marker;
    private LatLng position;
    private GoogleMap googleMap;

    private double longit;
    private double lat;
    private boolean camera = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        googleMap = mapView.getMap();
        googleMap.setMyLocationEnabled(true);

        if ((((ActivityMain) getActivity()).getClientItem().getLat() != 0) && (((ActivityMain) getActivity()).getClientItem().getLonget() != 0)) {
            setMarker();
            camera = false;
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.setOnMyLocationChangeListener(this);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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


        return view;
    }

    public void setMarker() {
        LatLng markerPosition = new LatLng(((ActivityMain) getActivity()).getClientItem().getLat(), ((ActivityMain) getActivity()).getClientItem().getLonget());
        marker = new MarkerOptions().position(
                markerPosition);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerPosition).zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

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
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSettings) {
            if (position != null) {
                dialogContact();
                ((ActivityMain) getActivity()).getClientItem().setLat(position.latitude);
                ((ActivityMain) getActivity()).getClientItem().setLonget(position.longitude);
            } else if ((((ActivityMain) getActivity()).getClientItem().getLat() != 0) && (((ActivityMain) getActivity()).getClientItem().getLonget() != 0)) {
                ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
            } else Toast.makeText(getActivity(), R.string.selectpoint, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.showcont)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 4);
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
    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        if (!permission[0].equals(Manifest.permission.READ_CONTACTS)) {
            ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
        } else {
            ((ActivityMain) getActivity()).showScreen(new FragmentPhone(), FragmentPhone.TAG, true);
        }
    }


}
