package com.example.uddd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uddd.Domains.PopularDomain;
import com.example.uddd.Domains.SavedDomain;
import com.example.uddd.R;

import java.util.ArrayList;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    ArrayList<SavedDomain> items;

    public SavedAdapter(ArrayList<SavedDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SavedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_saved_location, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedAdapter.ViewHolder holder, int position) {
        holder.savedLocationNameTxt.setText(items.get(position).getLocationName());
        holder.savedLocationTxt.setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView savedLocationTxt, savedLocationNameTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            savedLocationTxt = itemView.findViewById(R.id.savedLocationTxt);
            savedLocationNameTxt = itemView.findViewById(R.id.savedLocationNameTxt);
        }
    }
}
