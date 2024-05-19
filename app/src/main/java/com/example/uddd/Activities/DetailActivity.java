package com.example.uddd.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTxt, locationTxt,descriptionTxt, scoreTxt;
    private PopularDomain item;
    private ImageView picImg;
    private ImageButton backButton, yesButton, noButton;
    private ToggleButton likeButton;
    private CommentAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout askLayout;
    private ConstraintLayout commentLayout;
    private Button commentButton,cancelButton;
    private EditText comment;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        setVariable();
        InitTestInformation();
        InitCommentView();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        likeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(DetailActivity.this, R.drawable.heart_30px), null);
                else
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(DetailActivity.this, R.drawable.heart_black_30px), null);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askLayout.setVisibility(View.GONE);
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askLayout.setVisibility(View.GONE);
                commentLayout.setVisibility(View.VISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentLayout.setVisibility(View.GONE);
            }
        });
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to post this comment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                Date today = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedDate = dateFormat.format(today);
                                CommentDomain item = new CommentDomain("Anna Camile", comment.getText().toString(),
                                        ratingBar.getRating(),0,0,formattedDate,"avatar");
                                adapter.addItem(item);
                                commentLayout.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
    }

    private void initView()
    {
        titleTxt = findViewById(R.id.titleTxt);
        locationTxt = findViewById(R.id.locationTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
        backButton = findViewById(R.id.btn_back);
        picImg = findViewById(R.id.picImg);
        likeButton = findViewById(R.id.btn_like);
        yesButton = findViewById(R.id.btn_yes);
        noButton = findViewById(R.id.btn_no);
        askLayout = findViewById(R.id.ask_layout);
        commentLayout = findViewById(R.id.comment_layout);
        commentButton = findViewById(R.id.btn_comment);
        cancelButton = findViewById(R.id.btn_cancel);
        comment = findViewById(R.id.comment);
        ratingBar = findViewById(R.id.ratingBar);
    }
}