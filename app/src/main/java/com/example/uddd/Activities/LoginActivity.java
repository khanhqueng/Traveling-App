package com.example.uddd.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd.API.RetrofitClient;
import com.example.uddd.Models.User;
import com.example.uddd.R;

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
                // Hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                //Handle login
                String userEmail = usernameBar.getText().toString();
                String userPass = passwordBar.getText().toString();

                if(userEmail.isEmpty())
                {
                    showError("Username is empty.");
                    return;
                }

                if(userPass.isEmpty())
                {
                    showError("Password is empty.");
                    return;
                }
                Call<User> call = RetrofitClient.getInstance().getAPI().LoginUser(userEmail, userPass);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        //Toast.makeText(LoginActivity.this,"Connect successful",Toast.LENGTH_SHORT).show();

                        if(response.body() != null) {

                            // Get userID from sever
                            int userID = response.body().getUserID();

                            if(userID == 0)
                            {
                                showError("Incorrect username or password. Please Try again.");
                                return;
                            }

                            //Give userID to home page
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user",response.body());
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(LoginActivity.this,"No respone from sever",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"Fail to connect",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    public void showError(String content)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Error")
                .setMessage(content)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}