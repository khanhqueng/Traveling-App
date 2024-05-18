package com.example.uddd.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd.API.RetrofitClient;
import com.example.uddd.Models.User;
import com.example.uddd.databinding.RegisterPageBinding;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private RegisterPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = RegisterPageBinding.inflate((getLayoutInflater()));

        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName= binding.nameBar.getText().toString().trim();
                String userEmail= binding.userNameBar.getText().toString().trim();
                String userPass= binding.passwordBar.getText().toString().trim();
                String cfPass= binding.confirmPasswordBar.getText().toString().trim();
                if(userName.isEmpty() ||userEmail.isEmpty()|| userPass.isEmpty()|| cfPass.isEmpty()|| !(cfPass.equals(userPass))){
                    Toast.makeText(SignupActivity.this, "Please fill correct information", Toast.LENGTH_LONG).show();

                }
                else {
                    Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().createUser(userName, userEmail, userPass);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try{
                                String info= response.body().string();



                                Toast.makeText(SignupActivity.this,info,Toast.LENGTH_LONG).show();

                            }catch(IOException e){
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SignupActivity.this,"Failed to sign up",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });

    }
}