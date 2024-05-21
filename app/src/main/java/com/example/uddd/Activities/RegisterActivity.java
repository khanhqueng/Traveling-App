package com.example.uddd.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

public class RegisterActivity extends AppCompatActivity {

    ImageButton backButton, showButton1,showButton2;
    EditText nameBar,usernameBar,passwordBar,confirmBar;
    Button singupButton;
    TextView dobBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dobBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        dobBar.setText(" "+dayOfMonth+"/"+month+"/"+year);
                    }
                });
                datePickerDialog.show();
            }
        });
        showButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordBar.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    passwordBar.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    passwordBar.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        showButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmBar.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    confirmBar.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    confirmBar.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameBar.getText().toString();
                String username = usernameBar.getText().toString();
                String password = passwordBar.getText().toString();
                String dob = dobBar.toString().trim();

                if(!checkValidName(name)||!checkValidUsername(username)||!checkValidPassword())
                    return;

                //Handle register
                Call<User> call = RetrofitClient.getInstance().getAPI().createUser(name, username, password);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        int info= response.body().getId();
                        Toast.makeText(RegisterActivity.this,info,Toast.LENGTH_LONG).show();

                        //Give userID to home page
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        //intent.putExtra("userID",response.body().g);
                        startActivity(intent);

                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this,"Failed to sign up",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }
    void showError(String error)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    boolean checkValidName(String name)
    {
        if(name.isEmpty())
        {
            showError("Name is empty.");
            return false;
        }
        //Name does not contain number and special character
        String regex = "^[a-zA-Z]+$";
        if(!name.matches(regex))
        {
            showError("Invalid name. Name doesn't contain number and special character. Please try again.");
            return false;
        }
        return true;
    }

    boolean checkValidUsername(String username)
    {
        if(username.isEmpty())
        {
            showError("Username is empty.");
            return false;
        }
        //Username does not contain space
        String regex = "^[^\\s]+$";
        if(!username.matches(regex))
        {
            showError("Invalid username. Username doesn't contain space. Please try again.");
            return false;
        }
        return true;
    }
    boolean checkValidPassword()
    {
        String password = passwordBar.getText().toString();
        String confirm = confirmBar.getText().toString();
        if(password.isEmpty())
        {
            showError("Password is empty.");
            return false;
        }
        if(confirm.isEmpty())
        {
            showError("Confirm password is empty.");
            return false;
        }
        if(!password.equals(confirm))
        {
            showError("Password and confirmation of password aren't the same. Please check your password.");
            return false;
        }
        return true;
    }
    public void initView()
    {
        backButton = findViewById(R.id.btn_back);
        nameBar = findViewById(R.id.name_bar);
        usernameBar = findViewById(R.id.user_name_bar);
        passwordBar = findViewById(R.id.password_bar);
        confirmBar = findViewById(R.id.confirm_password_bar);
        singupButton = findViewById(R.id.btn_signup);
        showButton1 = findViewById(R.id.btn_show1);
        showButton2 = findViewById(R.id.btn_show2);
        dobBar = findViewById(R.id.dob_bar);
    }
}