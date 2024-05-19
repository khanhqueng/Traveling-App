package com.example.uddd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd.R;

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
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordBar.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    passwordBar.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    passwordBar.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameBar.getText().toString();
                String password = passwordBar.getText().toString();

                //Handle login
            }
        });

    }
}