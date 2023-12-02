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

    CameraOptions recenter = new CameraOptions.Builder()
            .center(
                    Point.fromLngLat(
                            120.9932,14.5648
                    )
            )
            .zoom(17.0)
            .build();
//    ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK){
//                        if (result.getData() != null){
//                            Directory data = new Directory(
//                                    result.getData().getStringExtra("name"),
//                                    (Point) result.getData().getSerializableExtra("point"),
//                                    result.getData().getStringExtra("location"),
//                                    result.getData().getParcelableExtra("imageUri").toString(),
//                                    result.getData().getStringExtra("tag")
//                            );
//                            // Add the point on the map
//                            addPointOnMap(data);
//                        }
//                    }
//                }
//            }
//    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_map, container, false);

        this.mapView = this.rootView.findViewById(R.id.mapView);
        this.iv_recenter = this.rootView.findViewById(R.id.recenter);
        this.mapView.getMapboxMap().loadStyleUri(Style.LIGHT);
        this.gesturesPlugin = GesturesUtils.getGestures(this.mapView);
        this.gesturesPlugin.addOnMapLongClickListener(this::onMapLongClick);

        this.iv_recenter.setOnClickListener(v -> {
            this.mapView.getMapboxMap().setCamera(this.recenter);
            Log.d("Re-Center","Recenter Pressed");
        });

        this.annotationPlugin = AnnotationPluginImplKt.getAnnotations(this.mapView);
        updateDataAndAdapter();
        return rootView;
    }

    @Override
    public boolean onMapLongClick(@NonNull Point point) {
        Intent addMarkerIntent = new Intent(getActivity(), AddMarkerActivity.class);
        addMarkerIntent.putExtra("point", point);
        startActivity(addMarkerIntent);
//        this.myActivityResultLauncher.launch(addMarkerIntent);
        return true;
    }

    private void addPointOnMap(Directory directory) {
        // Your existing logic from MainActivity for adding points on the map
        // Ensure you have necessary dependencies and imports in the Fragment
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(this.annotationPlugin, new AnnotationConfig());
        int icon = R.drawable.ic_landmark;
        if(directory.getTag().equals("Fast-Food Restaurant")){
            icon = R.drawable.ic_fast_food;
        }
        else if(directory.getTag().equals("Food Stall")){
            icon = R.drawable.ic_food_stall;
        }
        else if(directory.getTag().equals("Printing Service")){
            icon = R.drawable.ic_printer;
        }
        else if (directory.getTag().equals("ATM")) {
            icon = R.drawable.ic_atm;
        }
        else if (directory.getTag().equals("Trash Bin")) {
            icon = R.drawable.ic_trash_bins;
        }
        else if (directory.getTag().equals("Entertainment")) {
            icon = R.drawable.ic_entertainment;
        }
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(directory.getPoint().getLongitude(), directory.getPoint().getLatitude()))
                .withTextField(directory.getDirectoryName())
                .withTextLineHeight(2.5)
                .withTextAnchor(TextAnchor.TOP)
                .withIconImage(BitmapFactory.decodeResource(getResources(), icon));
        pointAnnotationManager.create(pointAnnotationOptions);
//        pointAnnotationManager.addClickListener(this :: onAnnotationClick);
//        Log.d("Add Point On Map", String.valueOf(pointAnnotationManager.getAnnotations().size()));
    }

    @Override
    public boolean onAnnotationClick(@NonNull PointAnnotation pointAnnotation) {
        Log.d("onAnnotationClick", pointAnnotation.getTextField());
        Toast.makeText(getActivity(), "onAnnotationClick", Toast.LENGTH_LONG).show();
        return true;
    }
    @Override
    public void onStart(){
        super.onStart();
        updateDataAndAdapter();
    }

    private void updateDataAndAdapter() {
        MyFirestoreReferences.getDirectoryCollectionReference()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Directory> directories = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                directories.add(document.toObject(Directory.class));
                            }

                            for(Directory directory : directories){
                                addPointOnMap(directory);
                            }

                        }
                    }
                });
    }
}

