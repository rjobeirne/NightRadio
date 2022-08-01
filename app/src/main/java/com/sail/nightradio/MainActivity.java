package com.sail.nightradio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    public ExoPlayer player;
    private String url, urlMel, urlRN, urlRRR, urlPBS;
    Boolean flagPaused = true;
    Boolean flagPlaying = false;
    int sleepTimer = 45;  // minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlMel = "http://live-radio01.mediahubaustralia.com/3LRW/mp3";
        urlRN = "http://live-radio01.mediahubaustralia.com/2RNW/mp3";
        urlRRR = "http://realtime.rrr.org.au/p13";
        urlPBS = "https://playerservices.streamtheworld.com/api/livestream-redirect/3PBS_FMAAC.m3u8";

        player = new ExoPlayer.Builder(this)
        .setMediaSourceFactory(
            new DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
                .build();

        View mNowPlayingLogo = findViewById(R.id.now_playing);
        TextView mNowPlayingText = findViewById(R.id.now_playing_text);

        final ToggleButton btnMel = findViewById(R.id.play_mel);
        final ToggleButton btnRN = findViewById(R.id.play_rn);
        final ToggleButton btnRRR = findViewById(R.id.play_rrr);
        final ToggleButton btnPBS = findViewById(R.id.play_pbs);
        final ToggleButton btnPlayStop = findViewById(R.id.play_button);

        btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);

        btnMel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNowPlayingLogo.setBackgroundResource(R.drawable.radio_melbourne);
                mNowPlayingText.setText("ABC Radio Melbourne");
                url = urlMel;
                if (flagPlaying){
                    stopPlaying();
                }
                btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                playRadio(url);
            }
        });

        btnRN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNowPlayingLogo.setBackgroundResource(R.drawable.radio_national);
                mNowPlayingText.setText("ABC Radio National");
                url = urlRN;
                if (flagPlaying){
                    stopPlaying();
                }
                btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                playRadio(url);
            }
        });

        btnRRR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNowPlayingLogo.setBackgroundResource(R.drawable.rrr);
                mNowPlayingText.setText("3RRR");
                url = urlRRR;
                if (flagPlaying){
                    stopPlaying();
                }
                btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                playRadio(url);
            }
        });

        btnPBS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNowPlayingLogo.setBackgroundResource(R.drawable.pbs);
                mNowPlayingText.setText("3PBS");
                url = urlPBS;
                if (flagPlaying){
                    stopPlaying();
                }
                btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                playRadio(url);
            }
        });

        btnPlayStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!flagPlaying) {
                    playRadio(url);
                    btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                } else {
                    pauseRadio();
                    btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);
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
        flagPlaying = true;
        SleepTimer();

    }

    public void pauseRadio() {
       if(player!=null) {
           player.pause();
           flagPlaying = false;
       }
    }

    public void stopPlaying() {
        if(player!=null){
            player.stop();
            flagPlaying = false;
        }
    }

    public void SleepTimer() {
        new CountDownTimer(sleepTimer * 60 * 1000, 1000) {
             public void onTick(long millisUntilFinished) {
             }
             public void onFinish() {
                 stopPlaying();
             }
         }.start();
    }

    // Only required for reader app
//    @Override
//    public void onBackPressed() {
//        stopPlaying();
//        finish();
//    }

}