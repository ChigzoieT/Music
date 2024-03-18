package com.chigozie.musicplayer;

import static com.chigozie.musicplayer.MainActivity.musicfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, next_btn, prev_btn, back_btn, shuffle_btn, repeat_btn;
    FloatingActionButton fab;
    SeekBar sb;
    int position = -1;
    ArrayList<MusicFiles>listsongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initView();
        getntent();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mcurrrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    sb.setProgress(mcurrrentPosition);
                    duration_played.setText(formatted(mcurrrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private String formatted(int mcurrrentPosition) {
        String totalout = "";
        String totalnew = "";
        String seconds = String.valueOf(mcurrrentPosition%60);
        String minuites = String.valueOf(mcurrrentPosition/60);
        totalout = minuites + ":" + seconds;
        totalnew = minuites + ":" + "0" + seconds;
        if(seconds.length() == 1){
            return totalnew;
        }

        return totalout;
    }

    private void getntent() {
        position = getIntent().getIntExtra("position", -1);
        listsongs = musicfiles;
        if(listsongs!=null){
            fab.setImageResource(R.drawable.pause_fill0_wght400_grad0_opsz24);
            uri = Uri.parse(listsongs.get(position).getPath());
        }

        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }

        sb.setMax(mediaPlayer.getDuration()/1000);
    }

    private void initView() {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlayed);
        duration_total = findViewById(R.id.durationTotal);
        next_btn = findViewById(R.id.id_next);
        prev_btn = findViewById(R.id.id_prev);
        back_btn = findViewById(R.id.back_btn);
        shuffle_btn = findViewById(R.id.id_shufffle);
        repeat_btn = findViewById(R.id.id_repeat);
        sb = findViewById(R.id.seekBar);
        fab = findViewById(R.id.play_pause);
    }
}