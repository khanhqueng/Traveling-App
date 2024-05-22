package com.example.uddd.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uddd.Models.User;
import com.example.uddd.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ViewProfileActivity extends AppCompatActivity {

    User user;
    ImageButton backButton;
    ImageView avatar;
    TextView totalComment, reliability;
    EditText name,dob,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        user = (User) getIntent().getSerializableExtra("user");

        if(user == null)
            finish();

        backButton = findViewById(R.id.btn_back);
        avatar = findViewById(R.id.imv_avatar);
        totalComment = findViewById(R.id.tv_comment);
        reliability = findViewById(R.id.tv_reliability);
        name = findViewById(R.id.name_bar);
        dob = findViewById(R.id.dob_bar);
        email = findViewById(R.id.email_bar);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this).load(Uri.parse(user.getAvatar())).into(avatar);
        totalComment.setText(String.valueOf(user.getTotalComment()));
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String roundedValue = df.format(user.getReliability()*100);
        reliability.setText(roundedValue+"%");
        name.setText(user.getName());
        dob.setText(user.getDob());
        email.setText(user.getEmail());
    }
}