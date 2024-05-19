package com.example.uddd.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.uddd.Adapters.PopularAdapter;
import com.example.uddd.Adapters.SavedAdapter;
import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.Domains.SavedDomain;
import com.example.uddd.R;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    PopularAdapter favouriteAdapter;
    SavedAdapter savedLocationAdapter;
    RecyclerView recyclerView;
    ImageButton addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favourite_page, container, false);

        initFavouriteLocation();
        initSavedLocation();

        addButton = view.findViewById(R.id.btn_add_location);

        recyclerView = view.findViewById(R.id.view_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(favouriteAdapter);

        recyclerView = view.findViewById(R.id.view_saved);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(savedLocationAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.create_saved_location_form);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.outline_shape);
                ImageButton closeButton = dialog.findViewById(R.id.btn_close);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button confirmButton = dialog.findViewById(R.id.btn_confirm);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Hide the keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

                        EditText locationNameBar = dialog.findViewById(R.id.savedLocationName_bar);
                        EditText locationBar = dialog.findViewById(R.id.savedLocation_bar);

                        String locationName = locationNameBar.getText().toString();
                        String location = locationBar.getText().toString();

                        if(checkInputInformation(location)) {
                            SavedDomain item = new SavedDomain(locationName, location);
                            savedLocationAdapter.addItem(item);
                            dialog.dismiss();
                        }
                        else
                            showErrorMessage();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    boolean checkInputInformation(String location)
    {
        return true;
    }

    void showErrorMessage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error")
                .setMessage("Invalid location. Please correct your location.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initFavouriteLocation()
    {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Nha Trang Beach","Nha Trang","Beautiful beach","popular_pic",3.9f));
        items.add(new PopularDomain("Hue Capital","Hue","Beautiful beach","hue",3.5f));
        items.add(new PopularDomain("Ha Long Bay","Quang Ninh","Beautiful beach","vhl",4.0f));
        favouriteAdapter = new PopularAdapter(items);
    }
    void initSavedLocation()
    {
        ArrayList<SavedDomain> items = new ArrayList<>();
        items.add(new SavedDomain("Home","23A Tran Van Duat, district 3, HCM city"));
        items.add(new SavedDomain("Work","90D Ho Van Loi, district 2, HCM city"));
        savedLocationAdapter = new SavedAdapter(items);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}