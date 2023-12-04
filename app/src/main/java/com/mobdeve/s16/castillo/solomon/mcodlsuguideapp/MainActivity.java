package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv_nav_bar;
    String userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            this.userRef = intent.getStringExtra(IntentKeys.USER_ID_KEY.name());
            Log.d("MainActivity", userRef);
            getUser();
        }


        this.bnv_nav_bar = findViewById(R.id.bnv_nav_bar);
        Fragment mapFragment = new MapFragment();
        Fragment directoryListFragment = new DirectoryListFragment();

        setFragment(mapFragment);
        bnv_nav_bar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_map){
                setFragment(mapFragment);
                return true;
            } else if (id == R.id.nav_directory_list) {
                setFragment(directoryListFragment);
                return true;
            } else if (id == R.id.nav_logout) {
                finish();
                return true;
            }
            return false;
        });
    }

    private void setFragment(Fragment fragment) {
        if (fragment instanceof DirectoryListFragment) {
            DirectoryListFragment directoryListFragment = (DirectoryListFragment) fragment;
            directoryListFragment.setUserReference(this.userRef);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_layout, fragment);
        transaction.commit();
    }

    private void getUser() {
        DocumentReference userRef = MyFirestoreReferences.getUserDocumentReference(this.userRef);
        Tasks.whenAllSuccess(userRef.get())
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        User user = ((DocumentSnapshot) list.get(0)).toObject(User.class);
                        welcomeMessage(user);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Get User Failed", e.toString());
                });
    }

    private void welcomeMessage(User user){
        Toast.makeText(this, "Welcome " + user.getUsername() + "!",Toast.LENGTH_LONG).show();
    }

}