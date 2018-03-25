package ru.geekbrains.evgeniy.itunesplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Evgeniy on 22.03.2018.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder>{

    List<ModelResponceResult> modelResponseResult;

    public ResultAdapter(List<ModelResponceResult> modelResponseResult) {
        this.modelResponseResult = modelResponseResult;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itunes_item, parent, false);
        return new ResultViewHolder(modelResponseResult, v);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        //return modelResponse.getResultCount();
        if (modelResponseResult == null)
            return 0;
        return modelResponseResult.size();
    }

}

class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView artistName;
    private TextView trackName;
    private ImageView thumb;
    List<ModelResponceResult> results;
    public ResultViewHolder(List<ModelResponceResult> results, View itemView) {
        super(itemView);
        this.results = results;
        artistName = itemView.findViewById(R.id.textView_artistName);
        trackName = itemView.findViewById(R.id.textView_trackName);
        thumb = itemView.findViewById(R.id.imageView_thumb);
        itemView.setOnClickListener(this);
    }

    void bind(int position) {
        ModelResponceResult modelResponceResult = results.get(position);
        artistName.setText(modelResponceResult.getArtistName());
        trackName.setText(modelResponceResult.getTrackName());
        String img60 = modelResponceResult.getArtworkUrl60();
        Picasso.with(thumb.getContext()).load(img60).into(thumb);
    }

    @Override
    public void onClick(View v) {
        ModelResponceResult modelResponceResult = results.get(this.getLayoutPosition());
        Intent playerIntent = new Intent(thumb.getContext(), PlayerActivity.class);
        playerIntent.putExtra(PlayerActivity.MEDIA_PREVIEW_TRACK_KEY, modelResponceResult.getPreviewUrl());
        playerIntent.putExtra(PlayerActivity.MEDIA_TRACK_NAME_KEY, modelResponceResult.getTrackName());
        playerIntent.putExtra(PlayerActivity.MEDIA_ALBUM_IMG_KEY, modelResponceResult.getArtworkUrl100());
        thumb.getContext().startActivity(playerIntent);
    }
}

