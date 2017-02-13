package com.example.asus.midtermalbumsearchexam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by asus on 12/02/2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Album> albums;

    public  AlbumsAdapter(Context mContext, ArrayList<Album> albums){
        this.mContext = mContext;
        this.albums = albums;
    }

    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumsAdapter.ViewHolder holder, int position) {
        if(!albums.get(position).getImage().isEmpty()){
            Glide.with(mContext).load(albums.get(position).getImage()).into(holder.image);
        }
        holder.title.setText(albums.get(position).getName());
        holder.artist.setText(albums.get(position).getArtist());

    }

    @Override
    public int getItemCount() {
        Log.e("kobe",albums.size()+" asds");
        return albums.size();

    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title;
        private TextView artist;

        public ViewHolder(final View itemView){
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri albumUri = Uri.parse(albums.get(getAdapterPosition()).getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, albumUri);
                    mContext.startActivity(websiteIntent);
                }
            });

        }
    }
}
