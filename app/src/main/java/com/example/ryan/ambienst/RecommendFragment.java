package com.example.ryan.ambienst;

import static com.example.ryan.ambienst.MainActivity.cards;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendFragment extends Fragment {



    public RecommendFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private BeatAdapter beatAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static MediaPlayer mMediaPlayer = Singleton.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public double jaccardSimilarity(String[] tags, String[] tags2){

        List<String> A = Arrays.asList(tags);
        List<String> B = Arrays.asList(tags2);

        double jaccardIndex = 0;

        int intersections = 0;
        double weightedIntersections = 0.0;
        int union = 0;


        for(String tag : A){
            if(B.contains(tag)){
                intersections++;
                weightedIntersections += 1/(Math.log(Math.E + A.indexOf(tag)));

                Log.d("weight: ", 1 / (Math.log(Math.E + A.indexOf(tag))) + ", " + A.indexOf(tag));
            }
        }



        //Counts the total number unique elements disregarding duplicates when combining the lists
        union = A.size() + B.size() - intersections;

        jaccardIndex = (double) weightedIntersections / union;

        return jaccardIndex;

    }

    public double bpmSimilarity(int bpm, int bpm2){
        int bpmDifference = Math.abs(bpm - bpm2);
        int bpmDifferenceMax = 75;
        double bpmWeight;

        Log.d("BPM Difference: ", String.valueOf(bpmDifference));

        if(bpmDifference < bpmDifferenceMax){
            bpmWeight = (double) bpmDifference/bpmDifferenceMax;
        }
        else{
            bpmWeight = 0.0;
        }
        return bpmWeight;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        Bundle data = getArguments();
        String savedBeatName = data.getString("Saved Beat Name");

        Card savedCard = Card.getCardByName2D(cards, savedBeatName);

        String[] savedCardTags = savedCard.getmTags();
        int savedCardBpm = savedCard.getmBpm();


        ArrayList<Card> similarBeats = new ArrayList<>();

        for (ArrayList<Card> list : cards.values()){
            for(Card card : list) {
                String[] tags = card.getmTags();
                int bpm = card.getmBpm();

                double similarity = jaccardSimilarity(tags, savedCardTags) * bpmSimilarity(bpm, savedCardBpm);
                Log.d("Jaccard Similarity: ", String.valueOf(jaccardSimilarity(tags, savedCardTags)));
                Log.d("BPM Similarity: ", String.valueOf(bpmSimilarity(bpm, savedCardBpm)));


                if (similarity > 0.02 && tags!=savedCardTags) {
                    similarBeats.add(card);
                    Log.d("Similarity: ", String.valueOf(similarity));
                }
            }
        }


        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        beatAdapter = new BeatAdapter(similarBeats, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(beatAdapter);

        beatAdapter.setOnItemClickListener(new BeatAdapter.OnItemClickListener() {

            boolean isPlaying = false;
            Card currentCard = null;

            @Override
            public void onItemClick(int position) {
                Card savedCard = similarBeats.get(position);
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
                            mainActivity.playIndividualCardSound(savedCard, "beat");
                            currentCard = savedCard;

                        }
                    }


                    isPlaying = false;
                }
                else{
                    mainActivity.playIndividualCardSound(savedCard, "beat");
                    currentCard = savedCard;
                    isPlaying = true;
                }
            }

            @Override
            public void onSaveClick(int position) {

            }
        });
        return view;
    }
}