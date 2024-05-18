package com.example.uddd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd.API.RetrofitClient;
import com.example.uddd.Models.User;
import com.example.uddd.databinding.HomePageBinding;
import com.example.uddd.databinding.LogInPageBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  {
    private HomePageBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        binding= HomePageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GGMapView.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
