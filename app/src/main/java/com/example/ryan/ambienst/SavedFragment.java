package com.example.ryan.ambienst;

import android.media.MediaPlayer;
import android.os.Bundle;

/*import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SavedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public SavedFragment() {
        // Required empty public constructor
    }



    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static ArrayList<Card> savedBeats = new ArrayList<>();

    private static MediaPlayer mMediaPlayer = Singleton.getInstance();

    private SeekBar savedSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        /*Bundle data = getArguments();
        String beatName = data.getString("Beats");

        savedBeats.add(Card.getCardByName(MainActivity.samples, beatName));*/



        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        adapter = new Adapter(savedBeats, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {

            boolean isPlaying = false;
            Card currentCard = null;
            @Override
            public void onItemClick(int position) {
                Card savedCard = savedBeats.get(position);
                MainActivity mainActivity = (MainActivity) getActivity();

                if (isPlaying){
                    if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                        if(currentCard.equals(savedCard)){

                            //If the same beat card is clicked, stop and reset the song

                            mMediaPlayer.stop();

                            /*mMediaPlayer.stop();
                            mMediaPlayer.reset();*/
                        }
                        else {

                            //If another beat card is clicked, don't stop the song but reset and play the other song
                            mMediaPlayer.reset();
                            mainActivity.playIndividualCardSound(savedCard, "beat", savedSeekBar);
                            currentCard = savedCard;

                        }
                    }


                    isPlaying = false;
                }
                else{
                    mainActivity.playIndividualCardSound(savedCard, "beat", savedSeekBar);
                    currentCard = savedCard;
                    isPlaying = true;
                }

            }
            @Override
            public void onButtonClick(int position){
                Card savedCard = savedBeats.get(position);

                Bundle beatName = new Bundle();
                beatName.putString("Saved Beat Name", savedCard.getLine1());

                RecommendFragment recommendFragment = new RecommendFragment();
                recommendFragment.setArguments(beatName);

                //getChildFragmentManager() Manages this fragment's children, used for nested fragments
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, recommendFragment)
                        .addToBackStack(null) // Enables back navigation
                        .commit();
            }

        });

        savedSeekBar = view.findViewById(R.id.seekBarSaved);

        mMediaPlayer.setOnPreparedListener(mp -> {
            savedSeekBar.setMax(mMediaPlayer.getDuration());

            Log.d("DEBUG", "Duration: " + mMediaPlayer.getDuration());

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    if (mMediaPlayer != null) {
                        savedSeekBar.setProgress(mMediaPlayer.getCurrentPosition(), true);
                    }

                }
            }, 0, 1000);
        });

        savedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });

        return view;
    }

    // Registers and updates SeekBar only when fragment active
    @Override
    public void onResume(){
        super.onResume();

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.registerSeekBar(savedSeekBar);
            mainActivity.updateSeekBars();
        }

    };

    @Override
    public void onPause(){
        super.onPause();

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            mainActivity.removeSeekBar(savedSeekBar);
        }
    }
}