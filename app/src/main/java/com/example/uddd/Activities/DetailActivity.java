package com.example.uddd.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uddd.Adapters.CommentAdapter;
import com.example.uddd.Domains.CommentDomain;
import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {


    private TextView titleTxt, locationTxt,descriptionTxt, scoreTxt;
    private PopularDomain item;
    private ImageView picImg;
    private ImageButton btn_back;

    private RecyclerView.Adapter adapter;
    private  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        setVariable();
        InitTestInformation();
        InitCommentView();
    }

    public void InitTestInformation()
    {
        ArrayList<CommentDomain> items = new ArrayList<>();
        items.add(new CommentDomain("Anna Watson","Regular beach",4,15,40,"1/2/2023","avatar"));
        items.add(new CommentDomain("Anna Watson","Regular beach",2,17,60,"7/2/2023","avatar"));
        items.add(new CommentDomain("Anna Watson","Regular beach",5,10,0,"15/9/2023","avatar"));
        adapter = new CommentAdapter(items);
    }

    public void InitCommentView()
    {
        recyclerView =  findViewById(R.id.view_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    private void setVariable()
    {
        item = (PopularDomain) getIntent().getSerializableExtra("object");

        titleTxt.setText(item.getTitle());
        scoreTxt.setText(Float.toString(item.getScore()));
        locationTxt.setText(item.getLocation());
        descriptionTxt.setText(item.getDescription());

        int drawableResId = getResources().getIdentifier(item.getPic(),"drawable",getPackageName());

        Glide.with(this)
                .load(drawableResId)
                .into(picImg);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView()
    {
        titleTxt = findViewById(R.id.titleTxt);
        locationTxt = findViewById(R.id.locationTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
        btn_back = findViewById(R.id.btn_back);
        picImg = findViewById(R.id.picImg);
    }
}