package com.example.trackify;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    private ImageButton btnPlay;
    private SeekBar seekBarAudio;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean reproduciendo = false;

    private final Runnable actualizarSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBarAudio.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        btnPlay = findViewById(R.id.BtnPlay);
        seekBarAudio = findViewById(R.id.SeekBarAudio);

        mediaPlayer = MediaPlayer.create(this, R.raw.trackify_audio);
        seekBarAudio.setMax(mediaPlayer.getDuration());

        btnPlay.setOnClickListener(v -> controlarAudio());

        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            reproduciendo = false;
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            seekBarAudio.setProgress(0);
        });
    }

    private void controlarAudio() {
        if (reproduciendo) {
            mediaPlayer.pause();
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            reproduciendo = false;
        } else {
            mediaPlayer.start();
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
            reproduciendo = true;
            handler.post(actualizarSeekBar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(actualizarSeekBar);

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}