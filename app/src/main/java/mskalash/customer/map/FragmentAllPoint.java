package mskalash.customer.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import mskalash.customer.ActivityMain;
import mskalash.customer.ClientItem;
import mskalash.customer.DatabaseAdapter;
import mskalash.customer.FragmentInfo;
import com.customer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class FragmentAllPoint extends Fragment {
    public static final String TAG = "FragmentAllPoint";
    private MapView mapView;
    private GoogleMap googleMap;
    public static ArrayList<ClientItem> arrayList;
    ClusterManager<ClusterMapItem> clusterManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        ((ActivityMain) getActivity()).toolbar.setTitle(R.string.app_name);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        googleMap = mapView.getMap();
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        arrayList = db.getMapData();
        setCluster();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ((ActivityMain) getActivity()).getClientItem().setId(id(marker.getTitle(), arrayList));
                ((ActivityMain) getActivity()).getClientItem().setCheck(true);
                ((ActivityMain) getActivity()).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);

            }
        });
        return view;
    }

    private void setCluster() {

        clusterManager = new ClusterManager<ClusterMapItem>(getActivity(), googleMap);
        googleMap.setOnCameraChangeListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        addItems();

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterMapItem>() {
            @Override
            public boolean onClusterClick(Cluster<ClusterMapItem> cluster) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        cluster.getPosition(), (float) Math.floor(googleMap
                                .getCameraPosition().zoom + 2)), 300,
                        null);
                return true;
            }
        });

    }

    private void addItems() {
        for (int i = 0; i < arrayList.size(); i++) {

            ClusterMapItem offsetItem = new ClusterMapItem(arrayList.get(i).getLat(), arrayList.get(i).getLonget(),
                    arrayList.get(i).getProfileName(), arrayList.get(i).getLast());
            clusterManager.setRenderer(new OwnRendering(getActivity(), googleMap, clusterManager));
            clusterManager.addItem(offsetItem);

        }
    }

    private int id(String title, ArrayList<ClientItem> arrayList) {
        for (ClientItem clientItem : arrayList) {

            if (clientItem.getProfileName().equals(title)) {
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