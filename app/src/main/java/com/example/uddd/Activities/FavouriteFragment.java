package com.example.uddd.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uddd.R;

public class FavouriteFragment extends Fragment {

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.favourite_page, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mainActivity.InitFavouriteView();
        mainActivity.InitSavedLocationView();
    }
}