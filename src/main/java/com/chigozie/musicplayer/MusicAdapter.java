package com.chigozie.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

/** @noinspection ALL*/
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    Context context;
    private ArrayList<MusicFiles>mfiles;

    MusicAdapter(Context context, ArrayList<MusicFiles>mfiles){
        this.context = context;
        this.mfiles = mfiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.filename.setText(mfiles.get(position).getTitle());
       /* byte[] image=getalbumart(mfiles.get(position).getPath());

        if(image != null){
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.albumart);
        }else {
            Glide.with(context).asBitmap()
                    .load(R.drawable.ic_launcher_background)
                    .into(holder.albumart);
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mfiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView filename;
        ImageView albumart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            filename = itemView.findViewById(R.id.music_file_name);
            albumart = itemView.findViewById(R.id.music_img);
        }
    }

   /* private byte[] getalbumart(String uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        return art;
    }*/
}
