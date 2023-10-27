package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        MapView map = rootView.findViewById(R.id.mapView);

        // Configure the map settings
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));

        // Set the initial map position to DLSU Manila
        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(14.5648, 120.9932); // DLSU Manila coordinates
        mapController.setCenter(startPoint);
        mapController.setZoom(17); // Zoom level, adjust as needed

        return rootView;
    }
}
