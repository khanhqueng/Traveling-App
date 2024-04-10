package com.example.uddd.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uddd.Adapters.PopularAdapter;
import com.example.uddd.Adapters.ResultAdapter;
import com.example.uddd.Adapters.SavedAdapter;
import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.Domains.SavedDomain;
import com.example.uddd.R;
import com.example.uddd.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private RecyclerView.Adapter adapterLocation,adapterSavedLocation, adapterResult;
    private  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitTestInformation();
        //setContentView(R.layout.show_result_page);
        //InitResultView();
        InitSavedLocation();
        InitHomePage();
    }
    private void InitHomePage()
    {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.favouriteFragment, R.id.profileFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
    }

    public void InitSavedLocation()
    {
        ArrayList<SavedDomain> items = new ArrayList<>();
        items.add(new SavedDomain("Home","23A Tran Van Duat, district 3, HCM city"));
        items.add(new SavedDomain("Work","90D Ho Van Loi, district 2, HCM city"));
        adapterSavedLocation = new SavedAdapter(items);
    }

    public void InitSavedLocationView()
    {
        recyclerView =  findViewById(R.id.view_saved);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapterSavedLocation);
    }

    public void InitTestInformation()
    {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Nha Trang Beach","Nha Trang","Beautiful beach","popular_pic",3.9f));
        items.add(new PopularDomain("Hue Capital","Hue","Beautiful beach","hue",3.5f));
        items.add(new PopularDomain("Ha Long Bay","Quang Ninh","Beautiful beach","vhl",4.0f));
        adapterLocation = new PopularAdapter(items);
        adapterResult = new ResultAdapter(items);
    }

    public void InitFavouriteView()
    {
        recyclerView = findViewById(R.id.view_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapterLocation);
    }
    public void InitPopularView()
    {
        recyclerView = findViewById(R.id.view_por);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapterLocation);
    }

    public void InitResultView()
    {
        recyclerView = findViewById(R.id.view_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapterResult);
    }

    public void log_in_btn_on_click(View view) {
        setContentView(R.layout.log_in_page);
    }
}