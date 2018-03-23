package ru.geekbrains.evgeniy.itunesplayer;

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
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.bind(modelResponseResult ,position);
    }

    @Override
    public int getItemCount() {
        //return modelResponse.getResultCount();
        if (modelResponseResult == null)
            return 0;
        return modelResponseResult.size();
    }
}

class ResultViewHolder extends RecyclerView.ViewHolder {

    private TextView artistName;
    private TextView trackName;
    private ImageView thumb;
    public ResultViewHolder(View itemView) {
        super(itemView);
        artistName = itemView.findViewById(R.id.textView_artistName);
        trackName = itemView.findViewById(R.id.textView_trackName);
        thumb = itemView.findViewById(R.id.imageView_thumb);
    }

    void bind(List<ModelResponceResult> results, int position) {
        ModelResponceResult modelResponceResult = results.get(position);
        artistName.setText(modelResponceResult.getArtistName());
        trackName.setText(modelResponceResult.getTrackName());
        String img60 = modelResponceResult.getArtworkUrl60();
        Picasso.with(thumb.getContext()).load(img60).into(thumb);
    }

}

