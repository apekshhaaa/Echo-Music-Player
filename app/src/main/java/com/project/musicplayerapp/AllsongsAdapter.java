package com.project.musicplayerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class AllsongsAdapter extends RecyclerView.Adapter<AllsongsAdapter.MyViewHolder>{
    Context context;
    ArrayList<Allsongs> allsongsArrayList;
    ArrayList<Allsongs> FavouritesArrayList;
    public AllsongsAdapter(Context context,ArrayList<Allsongs> allsongsArrayList,ArrayList<Allsongs> FavouritesArrayList) {
        this.context=context;
        this.allsongsArrayList=allsongsArrayList;
        this.FavouritesArrayList = FavouritesArrayList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Allsongs allsongs=allsongsArrayList.get(position);
        holder.songname.setText(allsongs.song_name);
        holder.artistname.setText(allsongs.artist_name);


        holder.itemView.setOnClickListener(v -> {
            Allsongs selectedSong = allsongsArrayList.get(position);
            // Navigate to the activity where the song is being played and pass the selected song data
            Intent intent = new Intent(context, SongPlayerActivity.class);
            intent.putExtra("SONG_NAME", selectedSong.song_name);
            intent.putExtra("ARTIST_NAME", selectedSong.artist_name);
            intent.putExtra("SONG_RESOURCE_ID", selectedSong.song_resource_id);
            intent.putParcelableArrayListExtra("FAVORITES_LIST", FavouritesArrayList);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allsongsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView songname, artistname;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            songname=itemView.findViewById(R.id.song_name);
            artistname=itemView.findViewById(R.id.artist_name);

        }
    }
}
