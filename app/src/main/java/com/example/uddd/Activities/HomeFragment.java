package com.example.uddd.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uddd.API.RetrofitClient;
import com.example.uddd.API.RetrofitMapbox;
import com.example.uddd.Adapters.PopularAdapter;
import com.example.uddd.Adapters.ResultAdapter;
import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.Models.PlacesInfo;
import com.example.uddd.Models.User;
import com.example.uddd.R;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private User user;
    private EditText searchBar;
    private SearchResultsView searchResultsView;
    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter;
    private boolean ignoreNextQueryUpdate = false;
    private static RecyclerView recommendRecyclerView, popularRecyclerView;
    private ImageView avatar;
    private static PopularAdapter recommendAdapter;
    private static ResultAdapter popularAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        user = MainActivity.getUser();
        avatar = view.findViewById(R.id.imv_avatar);

        String avatarDb;
        if(user == null || user.getAvatar().equals("none"))
            avatarDb = getString(R.string.basicAvatar);
        else
            avatarDb = user.getAvatar();

        Uri avatarUri = Uri.parse(avatarDb);
        Glide.with(this).load(avatarUri).into(avatar);

        intiLocationInfo();

        recommendRecyclerView = view.findViewById(R.id.view_recommend);
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        popularRecyclerView = view.findViewById(R.id.view_popular);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        searchBar = view.findViewById(R.id.search_bar);
        PlaceAutocomplete placeAutocomplete = PlaceAutocomplete.create();
        searchResultsView = view.findViewById(R.id.result_view);

        searchResultsView.initialize(new SearchResultsView.Configuration(new CommonSearchViewConfiguration()));
        placeAutocompleteUiAdapter = new PlaceAutocompleteUiAdapter(searchResultsView,placeAutocomplete);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(ignoreNextQueryUpdate)
                {
                    ignoreNextQueryUpdate = false;
                    return;
                }
                placeAutocompleteUiAdapter.search(s.toString(), new Continuation<Unit>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }
                    @Override
                    public void resumeWith(@NonNull Object o) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!searchResultsView.getAdapterItems().isEmpty() && !ignoreNextQueryUpdate)
                                    searchResultsView.setVisibility(View.VISIBLE);
                                else
                                    searchResultsView.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (searchBar.length() > 0 && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                confirmLocation();
                return true; // Consume the event
            }
            return false; // Let system handle the event
        }
    });

        placeAutocompleteUiAdapter.addSearchListener(new PlaceAutocompleteUiAdapter.SearchListener() {
            @Override
            public void onSuggestionsShown(@NonNull List<PlaceAutocompleteSuggestion> list) {

            }

            @Override
            public void onSuggestionSelected(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {
                searchBar.setText(placeAutocompleteSuggestion.getName());
                confirmLocation();
            }

            @Override
            public void onPopulateQueryClick(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {

            }
            @Override
            public void onError(@NonNull Exception e) {

            }
        });

        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus)
                searchResultsView.setVisibility(View.GONE);
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ignoreNextQueryUpdate = true;
    }

    public void confirmLocation()
    {
        ignoreNextQueryUpdate = true;
        searchResultsView.setVisibility(View.GONE);

        // Hide the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

        // Move cursor to the end
        searchBar.setSelection(searchBar.getText().length());

        Call<PlacesInfo> call = RetrofitMapbox.getInstance().getAPI().geocode(searchBar.getText().toString(),getString(R.string.mapbox_access_token));
        call.enqueue(new Callback<PlacesInfo>() {
            @Override
            public void onResponse(Call<PlacesInfo> call, Response<PlacesInfo> response) {
                if(response.body()!= null)
                {
                    // Get location coordination
                    PlacesInfo.Coordinates coord = response.body().getFeatures().get(0).getProperties().getCoordinates();
                    String location = coord.getLongitude()+","+coord.getLatitude();

                    // Change to result activity
                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                    intent.putExtra("location",location);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<PlacesInfo> call, Throwable t) {
                Toast.makeText(getContext(),"Fail to get location coordination from sever",Toast.LENGTH_SHORT).show();
            }
        });

    }
    static void intiLocationInfo()
    {
        Call<ArrayList<PopularDomain>> call = RetrofitClient.getInstance().getAPI().getRecommend();
        call.enqueue(new Callback<ArrayList<PopularDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<PopularDomain>> call, Response<ArrayList<PopularDomain>> response) {

                if(response.body() != null )
                {
                   //Toast.makeText(getContext(),"Load complete",Toast.LENGTH_SHORT).show();
                    recommendAdapter = new PopularAdapter(response.body());
                    recommendRecyclerView.setAdapter(recommendAdapter);
                }
                //Toast.makeText(getContext(),"Fail to respond",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ArrayList<PopularDomain>> call, Throwable t) {
                //Toast.makeText(getContext(),"Fail to connect server.", Toast.LENGTH_SHORT).show();
            }
        });

        Call<ArrayList<PopularDomain>> call2 = RetrofitClient.getInstance().getAPI().getPopular();
        call2.enqueue(new Callback<ArrayList<PopularDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<PopularDomain>> call, Response<ArrayList<PopularDomain>> response) {

                if(response.body() != null )
                {
                    //Toast.makeText(getContext(),"Load complete",Toast.LENGTH_SHORT).show();
                    popularAdapter = new ResultAdapter(response.body());
                    popularRecyclerView.setAdapter(popularAdapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PopularDomain>> call, Throwable t) {
                //Toast.makeText(getContext(),"Fail to connect server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void UpdateLocationInfo()
    {
        Call<ArrayList<PopularDomain>> call = RetrofitClient.getInstance().getAPI().getRecommend();
        call.enqueue(new Callback<ArrayList<PopularDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<PopularDomain>> call, Response<ArrayList<PopularDomain>> response) {
                if(response.body()!=null)
                    recommendAdapter = new PopularAdapter(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<PopularDomain>> call, Throwable t) {
                Toast.makeText(getContext(),"Fail to connect server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}