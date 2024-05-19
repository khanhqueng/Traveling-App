package com.example.uddd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.uddd.Domains.CommentDomain;
import com.example.uddd.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<CommentDomain> items;
    public CommentAdapter(ArrayList<CommentDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(items.get(position).getUsername());
        holder.commentTxt.setText(items.get(position).getComment());
        holder.dateTxt.setText(items.get(position).getDate());
        holder.num_like.setText(Integer.toString(items.get(position).getAgree()));
        holder.num_dislike.setText(Integer.toString(items.get(position).getDisagree()));
        holder.score_bar.setRating(items.get(position).getScore());

        int drawableResId = holder.itemView.getResources().getIdentifier(items.get(position).getPic(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResId)
                .transform(new FitCenter())
                .into(holder.avatar);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(CommentDomain item)
    {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, commentTxt, dateTxt ,num_like, num_dislike;
        RatingBar score_bar;
        ImageView avatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            commentTxt = itemView.findViewById(R.id.commentTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            num_like = itemView.findViewById(R.id.num_like);
            num_dislike  = itemView.findViewById(R.id.num_dislike);
            score_bar = itemView.findViewById(R.id.score_bar);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
