package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv_nav_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_layout, fragment);
        transaction.commit();
    }

}