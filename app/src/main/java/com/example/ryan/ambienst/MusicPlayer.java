package com.example.ryan.ambienst;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;

public class MusicPlayer extends Application{

    static MusicPlayer instance;
    public MediaPlayer mediaPlayer;

    private MusicPlayer() {
        // Private constructor to prevent instantiation
    }
    public static MusicPlayer getInstance() {

        if (instance == null)
        {
            instance = new MusicPlayer();

        }


        return instance;
    }

    public void play(String mp3, int volume){
        if (mediaPlayer != null && (mediaPlayer.isPlaying())) {
            try {
                mediaPlayer.reset();
                //mMediaPlayer.stop();


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Catching", Toast.LENGTH_SHORT).show();
            }
        }

        try {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(mp3));
            mediaPlayer.setVolume(volume, volume);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mp3));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stop(){
        mediaPlayer.stop();
        mediaPlayer.reset();
    }




}
