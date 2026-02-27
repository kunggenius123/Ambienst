package com.example.ryan.ambienst;

import android.widget.SeekBar;

import androidx.lifecycle.ViewModel;

public class ProgressBar extends ViewModel {
    public SeekBar progressBar;
    /*
    progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            //Makes sure the bar only seeks when a user clicks, otherwise the audio will stutter as it seeks everytime the audio progresses
            if(fromUser && mMediaPlayer!=null){
                mMediaPlayer.seekTo(progress);


            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });*/
}
