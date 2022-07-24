package com.sail.nightradio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener  {

    private MediaPlayer mediaPlayer;
    private String url, urlMel, urlRN;
    Boolean flagPaused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlMel = "http://live-radio01.mediahubaustralia.com/3LRW/mp3";
        urlRN = "http://live-radio01.mediahubaustralia.com/2RNW/mp3";

        mediaPlayer = new MediaPlayer();

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
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseRadio() {
       if(mediaPlayer!=null) {
           mediaPlayer.pause();
       }
    }

    public void stopPlaying() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
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