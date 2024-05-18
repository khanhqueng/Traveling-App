package com.example.uddd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd.R;
import com.example.uddd.databinding.IntroPageBinding;
import com.example.uddd.databinding.LogInPageBinding;

public class IntroActivity extends AppCompatActivity {
    private IntroPageBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= IntroPageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}