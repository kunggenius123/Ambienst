package com.example.ryan.ambienst;

import android.media.MediaPlayer;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.*;

/*import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/

import androidx.recyclerview.widget.RecyclerView;

import static com.example.ryan.ambienst.SavedFragment.savedBeats;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import com.google.android.material.slider.RangeSlider;
import com.yuyakaido.android.cardstackview.Direction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BeatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // Testing github change


    private static MediaPlayer mMediaPlayer = Singleton.getInstance();

    private RecyclerView recyclerView;

    private CardStackView cardStackView;

    private BeatAdapter beatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CardStackListener cardStackListener;

    private SeekBar beatsSeekBar;

    private ImageButton save;

    private RangeSlider rangeSlider;

    public ArrayList<Card> shownCards;

    public BeatsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    ArrayList<Card> filteredBeatsByBPM(float minBPM, float maxBPM, ArrayList<Card> beats){
        ArrayList<Card> newBeatCards = new ArrayList<>();
        for (Card beat : beats){
            if (beat.getmBpm() > minBPM && beat.getmBpm() < maxBPM){
                newBeatCards.add(beat);
            }
        }
        return newBeatCards;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beats, container, false);

        //Gets sample name sent from MainActivity
        Bundle data = getArguments();
        String sampleName = data.getString("Sample");

        Card sample = Card.getCardByName(MainActivity.samples, sampleName);

        ArrayList<Card> beatCards = MainActivity.cards.get(sample);
        shownCards = beatCards;

        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackView.setHasFixedSize(true);


        BeatAdapter.OnItemClickListener itemListener = new BeatAdapter.OnItemClickListener() {

            boolean isPlaying = false;
            Card currentCard = null;
            @Override
            public void onItemClick(int position) {
                Card beatCard = shownCards.get(position);
                MainActivity mainActivity = (MainActivity) getActivity();


                if (isPlaying){
                    if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                        if(currentCard.equals(beatCard)){

                            //If the same beat card is clicked, stop and reset the song

                            mMediaPlayer.stop();

                            /*mMediaPlayer.stop();
                            mMediaPlayer.reset();*/
                        }
                        else {

                            //If another beat card is clicked, don't stop the song but reset and play the other song
                            mMediaPlayer.reset();
                            mainActivity.playIndividualCardSound(beatCard, "beat");
                            currentCard = beatCard;

                        }
                    }


                    isPlaying = false;
                }
                else{
                    mainActivity.playIndividualCardSound(beatCard, "beat");
                    currentCard = beatCard;
                    isPlaying = true;
                }



            }
            @Override
            public void onSaveClick(int position){
                Card beatCard = shownCards.get(position);

                Log.d("Saved", beatCard.getLine1());

                if (!savedBeats.contains(beatCard)){
                    savedBeats.add(beatCard);
                }


                /*Bundle data = new Bundle();
                data.putString("Beat", beatCard.getLine1());

                SavedFragment savedFragment = new SavedFragment();
                savedFragment.setArguments(data);*/
            }

        };


        cardStackListener = new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                //int position = cardStackView.getTopPosition();
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {

                // If last card of deck swiped
                if(position== beatAdapter.getItemCount() - 1){
                    Log.d("End of deck", String.valueOf(position));

                    cardStackView.post(() ->{

                        // Resets shownCards to whole deck
                        shownCards = beatCards;

                        layoutManager = new CardStackLayoutManager(getContext(), cardStackListener);
                        cardStackView.setLayoutManager(layoutManager);

                        // Resets the adapter, resetting the card deck
                        beatAdapter = new BeatAdapter(shownCards, true);
                        cardStackView.setAdapter(beatAdapter);

                        // Needs new listener (refresh)
                        beatAdapter.setOnItemClickListener(itemListener);

                        rangeSlider.setValues(60f, 180f);

                    });


                }
            }
        };

        layoutManager = new CardStackLayoutManager(getContext(), cardStackListener);

        cardStackView.setLayoutManager(layoutManager);


        beatAdapter = new BeatAdapter(shownCards, true);
        cardStackView.setAdapter(beatAdapter);

        beatAdapter.setOnItemClickListener(itemListener);

        beatsSeekBar = view.findViewById(R.id.seekBarBeats);



        //Makes sure media player is prepared before setting seekBar max and progress
        mMediaPlayer.setOnPreparedListener(mp -> {
            beatsSeekBar.setMax(mMediaPlayer.getDuration());

            Log.d("DEBUG", "Duration: " + mMediaPlayer.getDuration());

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    if (mMediaPlayer != null) {
                        beatsSeekBar.setProgress(mMediaPlayer.getCurrentPosition(), true);
                    }

                }
            }, 0, 1000);
        });


        beatsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        rangeSlider = view.findViewById(R.id.range_slider);

        rangeSlider.setValues(60f, 180f);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> values = slider.getValues();
                float min = values.get(0);
                float max = values.get(1);

                shownCards = filteredBeatsByBPM(min, max, beatCards);
                beatAdapter = new BeatAdapter(shownCards);
                cardStackView.setAdapter(beatAdapter);

                beatAdapter.setOnItemClickListener(itemListener);
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
            mainActivity.registerSeekBar(beatsSeekBar);
            mainActivity.updateSeekBars();
        }

    };

    @Override
    public void onPause(){
        super.onPause();

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            mainActivity.removeSeekBar(beatsSeekBar);
        }
    }


}