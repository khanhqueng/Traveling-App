package com.example.uddd.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.uddd.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public final class GGMapView extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ggmap_view);
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady( GoogleMap p0) {

        googleMap = p0;
        LatLng sydney = new LatLng(-34.0, 151.0);

        googleMap.addMarker((new MarkerOptions()).position(sydney).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
