package com.example.uddd.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uddd.API.RetrofitMapbox;
import com.example.uddd.Adapters.PopularAdapter;
import com.example.uddd.Adapters.ResultAdapter;
import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.Models.PlacesInfo;
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
    private EditText searchBar;
    private SearchResultsView searchResultsView;
    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter;
    private boolean ignoreNextQueryUpdate = false;
    private RecyclerView recyclerView;
    private ResultAdapter popularAdapter;
    private PopularAdapter recommendAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        intiLocationInfo();

        recyclerView = view.findViewById(R.id.view_recommend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(recommendAdapter);

        recyclerView = view.findViewById(R.id.view_popular);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(popularAdapter);

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
                //Handle


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
    public void intiLocationInfo()
    {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Nha Trang Beach","Nha Trang","Beautiful beach","popular_pic",3.9f));
        items.add(new PopularDomain("Hue Capital","Hue","Vũng Tàu is a port city and the capital of Bà Rịa-Vũng Tàu Province, on a peninsula in southern Vietnam. Once a French colonial town, it’s now a popular seaside resort that draws many visitors from Ho Chi Minh City, who arrive by hydrofoil. Its long, busy stretch of sandy coast, including Front Beach and Pineapple Beach, has the verdant Small Mountain and Big Mountain as backdrop.","hue",3.5f));
        items.add(new PopularDomain("Ha Long Bay","Quang Ninh","Beautiful beach","vhl",4.0f));

        popularAdapter = new ResultAdapter(items);
        recommendAdapter = new PopularAdapter(items);
    }

}