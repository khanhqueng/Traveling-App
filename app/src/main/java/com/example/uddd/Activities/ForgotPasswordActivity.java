package com.example.uddd.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd.Domains.CommentDomain;
import com.example.uddd.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageButton backButton;
    Button sendButton;
    EditText emailBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backButton = findViewById(R.id.btn_back);
        sendButton = findViewById(R.id.btn_send);
        emailBar = findViewById(R.id.email_bar);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkValidMail(emailBar.getText().toString()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    builder.setTitle("Error")
                            .setMessage("Invalid email.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)  {dialog.dismiss();}
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    //Handle send code to reset password
                }
            }
        });
    }
    private boolean checkValidMail(String mail)
    {
        return true;
    }
}