package com.sail.nightradio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import wseemann.media.FFmpegMediaMetadataRetriever;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    public ExoPlayer player;
    private String url, urlMel, urlRN, urlRRR, urlPBS;
    Boolean flagPlaying = false;
    int sleepTimer = 45;  // minutes
    TextView mNowPlayingShowText;
    String nameShow;
    private ImageButton settingsBtn;
    Boolean sleepFunction;

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
        mNowPlayingShowText = findViewById(R.id.now_playing_text_show);

        final ToggleButton btnMel = findViewById(R.id.play_mel);
        final ToggleButton btnRN = findViewById(R.id.play_rn);
        final ToggleButton btnRRR = findViewById(R.id.play_rrr);
        final ToggleButton btnPBS = findViewById(R.id.play_pbs);
        final ToggleButton btnPlayStop = findViewById(R.id.play_button);

        settingsBtn = findViewById(R.id.button_settings);

        btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);

        // Settings and preferences
        // Send Toast message on short click
        settingsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                shortClick();
            }
        });

        // Go to settings page on long click
        settingsBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // opening a new intent to open settings activity.
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return false;
            }
        });

        btnMel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNowPlayingLogo.setBackgroundResource(R.drawable.radio_melbourne);
                mNowPlayingText.setText("ABC Radio 774");
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
                if (url != null) {
                    if (!flagPlaying) {
                        playRadio(url);
                        btnPlayStop.setBackgroundResource(R.drawable.outline_pause_circle_24);
                    } else {
                        pauseRadio();
                        btnPlayStop.setBackgroundResource(R.drawable.outline_play_circle_24);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get settings from preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sleepFunction = sharedPreferences.getBoolean("prefs_sleep_function",Boolean.parseBoolean("TRUE"));
        sleepTimer = Integer.parseInt(sharedPreferences.getString("prefs_listen_time", "45"));
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

        // load data file
        FFmpegMediaMetadataRetriever metaRetriever = new FFmpegMediaMetadataRetriever();
        metaRetriever.setDataSource(url);
        String icyMeta = metaRetriever.extractMetadata
                (String.valueOf(FFmpegMediaMetadataRetriever.METADATA_KEY_ICY_METADATA));
        if(icyMeta != null) {
            nameShow = icyMeta.substring(icyMeta.indexOf("='") + 2,icyMeta.indexOf("';"));
        } else {
            nameShow = null;
        }
        mNowPlayingShowText.setText(nameShow);
        if (sleepFunction) {
            SleepTimer();
        }
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

    @Override
    public void onBackPressed() {
        stopPlaying();
        finish();
    }

    public void shortClick() {
        Toast toast = Toast.makeText(this, "Long click to get to Settings", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        TextView text = (TextView) view.findViewById(android.R.id.message);
        /*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
        text.setTextColor(Color.parseColor("#FFFFFF"));
        toast.show();
    }
}