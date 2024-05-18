package com.example.uddd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd.API.RetrofitClient;
import com.example.uddd.Models.User;
import com.example.uddd.R;
import com.example.uddd.databinding.LogInPageBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private LogInPageBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= LogInPageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail= binding.userNameBar.getText().toString().trim();
                String userPass= binding.passwordBar.getText().toString().trim();
                Call<User> call = RetrofitClient.getInstance().getAPI().LoginUser(userEmail,userPass);
                call.enqueue(new Callback<User>(){

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(LoginActivity.this, response.body().getEmail(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
