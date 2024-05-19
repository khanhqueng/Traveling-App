package com.example.uddd.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uddd.Adapters.ResultAdapter;
import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.R;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView.Adapter resultAdapter;
    private RecyclerView recyclerView;
    private ImageButton backButton;
    private String searchLocation;
    private ArrayList<ToggleButton> filters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filters = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int filterId = getResources().getIdentifier("filter" + (i + 1), "id",getPackageName());
            ToggleButton button = findViewById(filterId);
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        button.setBackgroundDrawable(getDrawable(R.drawable.gradient_green_button));
                    else
                        button.setBackgroundDrawable(getDrawable(R.drawable.outline_shape));
                }
            });
            filters.add(button);
        }

        getResultInfo();
        initResultView();

    }
    private void initResultView()
    {
        recyclerView = findViewById(R.id.view_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(resultAdapter);

        //test = findViewById(R.id.test_tv);
        ///test.setText(searchLocation);
    }

    public void getResultInfo()
    {
        searchLocation = getIntent().getStringExtra("location");

        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Nha Trang Beach","Nha Trang","Beautiful beach","popular_pic",3.9f));
        items.add(new PopularDomain("Hue Capital","Hue","Beautiful beach","hue",3.5f));
        items.add(new PopularDomain("Ha Long Bay","Quang Ninh","Beautiful beach","vhl",4.0f));

        resultAdapter = new ResultAdapter(items);
    }
}