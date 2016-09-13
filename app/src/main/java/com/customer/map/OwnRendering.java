package com.customer.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class OwnRendering extends DefaultClusterRenderer<ClusterMapItem> {
Context context;
    public OwnRendering(Context context, GoogleMap map,
                        ClusterManager<ClusterMapItem> clusterManager) {

        super(context, map, clusterManager);
        this.context=context;
    }


    protected void onBeforeClusterItemRendered(ClusterMapItem item, MarkerOptions markerOptions) {

        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onClusterRendered(Cluster<ClusterMapItem> cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterMapItem> cluster) {
        return cluster.getSize() > 1;
    }
}