package com.project.musicplayerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {
    Context context;

    ArrayList<Favourites> FavouritesArrayList;

    public FavouritesAdapter(Context context,ArrayList<Favourites> FavouritesArrayList) {
        this.context=context;
        this.FavouritesArrayList=FavouritesArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Favourites favourites=FavouritesArrayList.get(position);
        holder.songname.setText(favourites.song_name);
        holder.artistname.setText(favourites.artist_name);

    }

    @Override
    public int getItemCount() {
        return FavouritesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView songname,artistname;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songname=itemView.findViewById(R.id.song_name);
            artistname=itemView.findViewById(R.id.artist_name);
        }
    }
}
