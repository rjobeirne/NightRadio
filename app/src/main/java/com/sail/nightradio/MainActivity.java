package com.sail.nightradio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener  {

    private MediaPlayer mediaPlayer;
    public ExoPlayer player;
    private String url, urlMel, urlRN;
    Boolean flagPaused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlMel = "http://live-radio01.mediahubaustralia.com/3LRW/mp3";
        urlRN = "http://live-radio01.mediahubaustralia.com/2RNW/mp3";

//        mediaPlayer = new MediaPlayer();
        player = new ExoPlayer.Builder(this)
        .setMediaSourceFactory(
            new DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
                .build();

        final ToggleButton btnMel = (ToggleButton)findViewById(R.id.play_mel);
        final ToggleButton btnRN = (ToggleButton)findViewById(R.id.play_rn);
        btnMel.setBackgroundResource(R.drawable.outline_play_circle_24);
        btnRN.setBackgroundResource(R.drawable.outline_play_circle_24);

        btnMel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    btnMel.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        playRadio(urlMel);
                    } else {
                        pauseRadio();
                    }
                    flagPaused = false;
                } else {
                    btnMel.setBackgroundResource(R.drawable.outline_play_circle_24);
                    pauseRadio();
                    flagPaused = true;
                }
            }
        });

        btnRN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    btnRN.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    if (flagPaused) {
                        playRadio(urlRN);
                    } else {
                        pauseRadio();
                    }
                    flagPaused = false;
                } else {
                    btnRN.setBackgroundResource(R.drawable.outline_play_circle_24);
                    pauseRadio();
                    flagPaused = true;
                }
            }
        });
    }

    public void playRadio(String url) {
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(url)
                .setLiveConfiguration(
                    new MediaItem.LiveConfiguration.Builder()
                        .setMaxPlaybackSpeed(1.02f)
                        .build())
                .build();
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

    }

    public void pauseRadio() {
       if(player!=null) {
           player.pause();
       }
    }

    public void stopPlaying() {
        if(player!=null){
            player.stop();
        }
    }
    @Override
    public void onBackPressed() {
        stopPlaying();
        finish();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}