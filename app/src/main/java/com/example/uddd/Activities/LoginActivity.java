package com.example.uddd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uddd.R;
import com.example.uddd.API.RetrofitClient;
import com.example.uddd.Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageButton backButton, showButton;
    private TextView forgotPassword;
    private Button loginButton;
    private EditText usernameBar, passwordBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.btn_back);
        forgotPassword = findViewById(R.id.label_forgot);
        loginButton = findViewById(R.id.btn_login);
        showButton = findViewById(R.id.btn_show);
        usernameBar = findViewById(R.id.user_name_bar);
        passwordBar = findViewById(R.id.password_bar);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordBar.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    passwordBar.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    passwordBar.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = usernameBar.getText().toString();
                String userPass = passwordBar.getText().toString();

                //Handle login
                Call<User> call = RetrofitClient.getInstance().getAPI().LoginUser(userEmail,userPass);
                call.enqueue(new Callback<User>(){
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(LoginActivity.this, response.body().getEmail(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}