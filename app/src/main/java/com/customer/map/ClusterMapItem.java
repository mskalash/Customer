package com.customer.map;

import com.google.android.gms.maps.model.LatLng;

public class ClusterMapItem implements com.google.maps.android.clustering.ClusterItem {
    private final LatLng mPosition;
    private String title;
    private String snippet;

    public ClusterMapItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        this.title=title;
        this.snippet=snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getTitle() {
        return title;
    }
}