package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapLongClickListener, OnPointAnnotationClickListener {
    GesturesPlugin gesturesPlugin;
    MapView mapView;
    View rootView;
    ImageButton iv_recenter;
    AnnotationPlugin annotationPlugin;
    PointAnnotationManager pointAnnotationManager;

    CameraOptions recenter = new CameraOptions.Builder()
            .center(
                    Point.fromLngLat(
                            120.9932,14.5648
                    )
            )
            .zoom(17.0)
            .build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_map, container, false);

        this.mapView = this.rootView.findViewById(R.id.mapView);
        this.iv_recenter = this.rootView.findViewById(R.id.recenter);
        this.mapView.getMapboxMap().loadStyleUri(Style.LIGHT);
        this.gesturesPlugin = GesturesUtils.getGestures(this.mapView);
        this.gesturesPlugin.addOnMapLongClickListener(this);

        this.iv_recenter.setOnClickListener(v -> {
            this.mapView.getMapboxMap().setCamera(this.recenter);
            Log.d("Re-Center","Recenter Pressed");
        });

        this.annotationPlugin = AnnotationPluginImplKt.getAnnotations(this.mapView);
        this.pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(this.annotationPlugin, new AnnotationConfig());
        return rootView;
    }

    @Override
    public boolean onMapLongClick(@NonNull Point point) {
        Intent addMarkerIntent = new Intent(getActivity(), AddMarkerActivity.class);
        addMarkerIntent.putExtra("point", point);
        startActivity(addMarkerIntent);
        return true;
    }

    private void addPointOnMap(Directory directory) {
        // Your existing logic from MainActivity for adding points on the map
        // Ensure you have necessary dependencies and imports in the Fragment

        int icon = R.drawable.ic_landmark;
        switch (directory.getTag()) {
            case "Fast-Food Restaurant":
                icon = R.drawable.ic_fast_food;
                break;
            case "Food Stall":
                icon = R.drawable.ic_food_stall;
                break;
            case "Printing Service":
                icon = R.drawable.ic_printer;
                break;
            case "ATM":
                icon = R.drawable.ic_atm;
                break;
            case "Trash Bin":
                icon = R.drawable.ic_trash_bins;
                break;
            case "Entertainment":
                icon = R.drawable.ic_entertainment;
                break;
        }

        JsonObject directoryJson = new JsonObject();
        directoryJson.addProperty("directoryRef", directory.getDirectoryRef().getId());
        directoryJson.addProperty("name", directory.getDirectoryName());
        directoryJson.addProperty("location", directory.getLocation());

        JsonObject pointJson = new JsonObject();
        pointJson.addProperty("longitude", directory.getPoint().getLongitude());
        pointJson.addProperty("latitude", directory.getPoint().getLatitude());

        directoryJson.add("point", pointJson);

        JsonElement directoryData = new Gson().toJsonTree(directoryJson);

        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(directory.getPoint().getLongitude(), directory.getPoint().getLatitude()))
                .withTextField(directory.getDirectoryName())
                .withTextLineHeight(2.5)
                .withTextAnchor(TextAnchor.TOP)
                .withData(directoryData)
                .withIconImage(BitmapFactory.decodeResource(getResources(), icon));
        pointAnnotationManager.create(pointAnnotationOptions);
        pointAnnotationManager.addClickListener(this);
        Log.d("PointAnnotationManager", String.valueOf(this.pointAnnotationManager.getAnnotations().size()));
    }

    @Override
    public boolean onAnnotationClick(@NonNull PointAnnotation pointAnnotation) {
        if(!pointAnnotation.isDraggable()){
            Toast.makeText(this.rootView.getContext(), "You can drag the point", Toast.LENGTH_LONG).show();
            pointAnnotation.setDraggable(true);
        }
        else{
            pointAnnotation.setDraggable(false);
            JsonElement currentPoint = pointAnnotation.getData();
            if (currentPoint != null && currentPoint.isJsonObject()) {
                JsonObject annotationData = currentPoint.getAsJsonObject();

                // Extract the directoryRef value
                if (annotationData.has("directoryRef")) {
                    String directoryRefString = annotationData.get("directoryRef").getAsString();
                    Intent editMarkerIntent = new Intent(getActivity(), EditMarkerActivity.class);

                    Log.d("onAnnotationClick", annotationData.toString());
                    Log.d("onAnnotationClick", pointAnnotation.getPoint().toString());

                    editMarkerIntent.putExtra("point", pointAnnotation.getPoint());
                    editMarkerIntent.putExtra("directoryRef", directoryRefString);
                    startActivity(editMarkerIntent);
                }
            }
        }


        return true;
    }
    @Override
    public void onStart(){
        super.onStart();
        this.pointAnnotationManager.deleteAll();
        updateDataAndPoint();
    }


    private void updateDataAndPoint() {
        MyFirestoreReferences.getDirectoryCollectionReference()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Directory> directories = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                directories.add(document.toObject(Directory.class));
                            }

                            for (Directory directory : directories) {
                                addPointOnMap(directory);
                            }

                        }
                    }
                });
    }
}

