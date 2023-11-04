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
    ArrayList<Allsongs> favouritesList;

    public FavouritesAdapter(Context context,ArrayList<Allsongs> FavouritesArrayList) {
        this.context=context;
        this.favouritesList=FavouritesArrayList;

    }

    public void setFavourites(ArrayList<Allsongs> FavouritesArrayList){
        this.favouritesList = FavouritesArrayList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Allsongs song = favouritesList.get(position);
        holder.songname.setText(song.song_name);
        holder.artistname.setText(song.artist_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i;
                //not implemented
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
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
