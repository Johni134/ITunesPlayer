package ru.geekbrains.evgeniy.itunesplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

public class PlayerActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener {

    final static String MEDIA_PREVIEW_TRACK_KEY = "media_preview_track";
    final static String MEDIA_ALBUM_IMG_KEY = "media_album_img";
    final static String MEDIA_TRACK_NAME_KEY = "media_track_name";

    private String mediaAlbumImg = "";
    private String mediaPrevTrack = "";
    private String mediaTrackName = "";

    private ImageView imageViewAlbum;
    private ImageButton imageButtonPlay;
    private ImageButton imageButtonPause;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        imageViewAlbum = findViewById(R.id.imageView_album);

        imageButtonPlay = findViewById(R.id.imageButton_play);
        imageButtonPlay.setOnClickListener(this);

        imageButtonPause = findViewById(R.id.imageButton_pause);
        imageButtonPause.setOnClickListener(this);

        seekBar = findViewById(R.id.seekBar_player);
        seekBar.setMax(99);
        seekBar.setOnSeekBarChangeListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

        Intent intent = getIntent();
        if(intent != null) {
            mediaAlbumImg = intent.getStringExtra(MEDIA_ALBUM_IMG_KEY);
            mediaPrevTrack = intent.getStringExtra(MEDIA_PREVIEW_TRACK_KEY);
            mediaTrackName = intent.getStringExtra(MEDIA_TRACK_NAME_KEY);

            Picasso.with(this).load(mediaAlbumImg).into(imageViewAlbum);

            try {
                mediaPlayer.setDataSource(mediaPrevTrack);
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // show buffering
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // when song playing comlete
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageButton_play) {
            if(!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                primarySeekBarProgressUpdater();
            }
        }
        else if(v.getId() == R.id.imageButton_pause) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                primarySeekBarProgressUpdater();
            }
        }
    }

    private void primarySeekBarProgressUpdater() {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/
                mediaFileLengthInMilliseconds)*100));
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * progress;
        Log.i("TOUCH_SEEK", String.valueOf(playPositionInMillisecconds));
        mediaPlayer.seekTo(playPositionInMillisecconds);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBackPressed() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }
}
