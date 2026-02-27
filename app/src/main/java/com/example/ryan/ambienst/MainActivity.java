package com.example.ryan.ambienst;

import static android.app.PendingIntent.getActivity;

import android.content.*;
import android.media.*;
import android.os.Bundle;
import android.speech.RecognizerIntent;

/*import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/

import android.util.Log;
import android.widget.RelativeLayout;

//import android.support.v7.app.AppCompatActivity;

import android.view.*;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.*;

import java.io.IOException;

//import java.util.ArrayList;
//import java.util.Locale;
import java.io.InputStream;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout screen;

    //private static MusicPlayer mMediaPlayer = MusicPlayer.getInstance();

    private static MediaPlayer mMediaPlayer = Singleton.getInstance();

    private RecyclerView recyclerView;

    //Adapter inherits RecyclerViewAdapter, it is a modified version for the cards for this app
    private Adapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    Adapter.OnItemClickListener listener;

    public static Map<Card, ArrayList<Card>> cards;

    public static ArrayList<Card> samples;

    public static ArrayList<Card> shownSamples;

    public SeekBar mainSeekBar;

    private Timer timer;

    // Creates centralized list of all the fragments/activity seekbars
    public ArrayList<SeekBar> activeSeekBars = new ArrayList<>();

    SearchView searchView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (RelativeLayout) findViewById(R.id.layout);

        //2D ArrayList
        cards = new LinkedHashMap<>();

        //Make a 2D ArrayList for all the sample beats, each row is a different sample
        ArrayList<ArrayList<Card>> sampleBeats = new ArrayList<>();

        sampleBeats.add(new ArrayList<>()); //Index 0 of 2D ArrayList

        sampleBeats.get(0).add(new Card(
                R.drawable.ign1s_logo, "Interstellar prod.Ign1s", 136, "Travis Scott Type Beat", R.raw.interstellar_beat_ign1s,
                new String[]{"Travis Scott", "Nostalgic", "Chill", "Inspirational"}));

        sampleBeats.get(0).add(new Card(R.drawable.interstellar_thumbnail_andyr, "Interstellar prod.Andyr", 202, "Lil Durk Type Beat", R.raw.interstellar_beat_andyr,
                new String[]{"Lil Durk", "Piano", "Emotional", "Nostalgic"}));

        sampleBeats.get(0).add(new Card(R.drawable.interstellar_movie_wide, "Interstellar prod.frgtn", 140, "Travis Scott x Han Zimmer Type Beat", R.raw.interstellar_beat_frgtn,
                new String[]{"Travis Scott", "Han Zimmer", "Inspirational", "Cinematic", "Strings", "Hard"}));

        sampleBeats.add(new ArrayList<>()); //Index 1 of 2D ArrayList

        sampleBeats.get(1).add(new Card(R.drawable.killing_me_softly_jaaybo, "Killing Me Softly prod.Ign1s", 94, "EBK JaayBo Type Beat", R.raw.killingmesoftly_beat_ign1s,
                new String[]{"Cali", "EBK JaayBo", "Soulful", "Hard"}));

        sampleBeats.get(1).add(new Card(R.drawable.killingmesoftly_poster, "Killing Me Softly prod.Sav", 100, "Flint Type Beat", R.raw.killingmesoftly_beat_sav,
                new String[]{"Detroit", "Peezy", "Bouncy", "Soulful"}));

        sampleBeats.get(1).add(new Card(R.drawable.killingmesoftly_poster, "Killing Me Softly prod. Tal6y Beats II", 100, "Flint Type Beat", R.raw.killingmesoftly_beat_sav,
                new String[]{"Trap", "21 Savage", "Dark", "Soulful", "Piano"}));

        sampleBeats.add(new ArrayList<>()); //Index 2 of 2D ArrayList
        sampleBeats.get(2).add(new Card(R.drawable.verde_babii_nerd, "Sincerely prod.Ign1s", 92, "EBK Bckdoe Type Beat", R.raw.sincerely_beat,
                new String[]{"Cali", "Verde Babii", "Upbeat", "Bouncy"}));
        sampleBeats.get(2).add(new Card(R.drawable.goodfellas_poster, "Goodfellas prod.Sav", 98, "Flint Type Beat", R.raw.goodfellas_beat_sav,
                new String[]{"Flint", "Rio", "Detroit", "Cinematic", "Strings", "Bouncy", "Dark"}));
        sampleBeats.get(2).add(new Card(R.drawable.goodfellas_arrest, "Goodfellas prod. Floc Rosa", 137, "Future Type Beat", R.raw.goodfellas_type_beat_floc_rosa,
                new String[]{"Trap", "Future", "Hard", "Dark", "Piano"}));

        sampleBeats.add(new ArrayList<>()); //Index 3 of 2D ArrayList
        sampleBeats.get(3).add(new Card(R.drawable.geeked_up_thumbnail, "Geeked Up prod.Ign1s", 97, "EBK Bckdoe Type Beat", R.raw.geeked_up_beat_ign1s,
                new String[]{"EBK Bckdoe", "Flint", "Cali", "Hard", "Bouncy"}));
        sampleBeats.get(3).add(new Card(R.drawable.geeked_up_thumbnail, "Geeked Up prod.Reuel", 104, "Flint Type Beat", R.raw.geeked_up_beat_reuel,
                new String[]{"Detroit", "Rio", "Bouncy", "Piano", "Hard"}));
        sampleBeats.get(3).add(new Card(R.drawable.geeked_up_thumbnail, "Geeked Up prod.X5", 91, "Von Off 1700 Type Beat", R.raw.geeked_up_beat_vonoff1700,
                new String[]{"Trap", "Chicago", "Von Off 1700", "Bouncy"}));

        sampleBeats.add(new ArrayList<>()); //Index 4 of 2D ArrayList
        sampleBeats.get(4).add(new Card(R.drawable.zoey101_thumbnail, "Zoey 101 prod. Ign1s", 116, "Cash Kidd Type Beat", R.raw.zoey101_ign1s,
                new String[]{"Detroit", "Cash Kidd", "Upbeat", "Bouncy", "Hard"}));
        sampleBeats.get(4).add(new Card(R.drawable.zoey101_thumbnail, "Zoey 101 prod. Kay Archon", 118, "Chopped Screwed Type Beat", R.raw.zoey101_beat_archon,
                new String[]{"Trap", "Upbeat", "Bouncy", "Hard"}));
        sampleBeats.get(4).add(new Card(R.drawable.zoey101_thumbnail, "Zoey 101 prod. @Lowkeymali x @prodyeahitis", 144, "sha ek x edot baby sample drill type beat", R.raw.zoey101_beat_drill,
                new String[]{"Drill", "Hard", "Upbeat", "Bouncy"}));

        sampleBeats.add(new ArrayList<>()); //Index 5 of 2D ArrayList
        sampleBeats.get(5).add(new Card(R.drawable.should_have_cheated_thumbnail, "Should Have Cheated prod. @tazzomadeit x @prod.lulzaye", 89, "EBK JaayBo Type Beat", R.raw.should_have_cheated_jaaybo,
                new String[]{"Cali", "EBK JaayBo", "Bouncy", "Dark"}));
        sampleBeats.get(5).add(new Card(R.drawable.should_have_cheated_thumbnail, "Should Have Cheated prod. Buddy Loves Beatz", 80, "R&B Sample Type Beat", R.raw.should_have_cheated_buddy_loves_beatz,
                new String[]{"Trap", "Bouncy", "Hard", "Dark"}));


        cards.put(new Card(R.drawable.interstellar_movie_wide, "Interstellar", "We used to look up at the sky and wonder at our place in the stars", R.raw.interstellar), sampleBeats.get(0));
        cards.put(new Card(R.drawable.killingmesoftly_poster, "Killing Me Softly", "Strumming my pain with his fingers", R.raw.killingmesoftly), sampleBeats.get(1));
        cards.put(new Card(R.drawable.goodfellas_poster, "Goodfellas", "We always called each other Goodfellas", R.raw.sincerely), sampleBeats.get(2));
        cards.put(new Card(R.drawable.geeked_up_thumbnail, "Geeked Up", "Remix!", R.raw.geeked_up), sampleBeats.get(3));
        cards.put(new Card(R.drawable.zoey101_thumbnail, "Zoey 101", "Disney", R.raw.zoey101), sampleBeats.get(4));
        cards.put(new Card(R.drawable.should_have_cheated_thumbnail, "Should Have Cheated", "Musn't you accuse me of", R.raw.should_have_cheated), sampleBeats.get(5));


        samples = new ArrayList<>(cards.keySet());

        shownSamples = samples;


        JSONArray jsonArray = new JSONArray();


        /*
        cards.add(new Card(R.drawable.goodfellas_poster, "Goodfellas", "We always called each other Goodfellas", R.raw.sincerely, R.raw.sincerely_beat));
        cards.add(new Card(R.drawable.killingmesoftly_poster, "Killing Me Softly", "Strumming my pain with his fingers", R.raw.killingmesoftly, R.raw.killingmesoftly_beat));
        */

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new Adapter(shownSamples, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        Log.d("Samples 0: ", samples.get(0).getLine1());
        Log.d("Samples 1: ", samples.get(1).getLine1());
        Log.d("Samples 2: ", samples.get(2).getLine1());

        listener = new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Card mCard = cards.get(position);

                if(mainSeekBar.getVisibility() == View.GONE) {
                    mainSeekBar.setVisibility(View.VISIBLE);
                }

                Card mCard = shownSamples.get(position);

                Log.d("Card Clicked", mCard.getLine1()+ "Position: " + position);

                /*switch (mCard.getLine1()) {
                    case "Cricket":
                        playIndividualCardSound(mCard, "sound_effect");
                        break;
                }*/

                playIndividualCardSound(mCard, "sample", mainSeekBar);

            }

            @Override
            public void onButtonClick(int position) {

                if(mainSeekBar.getVisibility() == View.VISIBLE) {
                    mainSeekBar.setVisibility(View.GONE);
                }
                //Card mCard = cards.get(position);

                Card mCard = shownSamples.get(position);

                Bundle data = new Bundle();
                data.putString("Sample", mCard.getLine1());

                BeatsFragment beatsFragment = new BeatsFragment();
                beatsFragment.setArguments(data);

                /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, beatsFragment);
                transaction.addToBackStack(null);  // Optional, to add the fragment to the back stack
                transaction.commit();*/

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, beatsFragment)
                        .addToBackStack(null) // Enables back navigation
                        .commit();


                //playIndividualCardSound(mCard, "remix");
            }
        };

        adapter.setOnItemClickListener(listener);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId()==R.id.samples){
                getSupportFragmentManager().popBackStack();
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                return true;

            }
            /*else if (menuItem.getItemId()==R.id.beats){
                return true;
            }*/
            else if (menuItem.getItemId()==R.id.saved) {
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SavedFragment())
                        .addToBackStack(null) // Enables back navigation
                        .commit();
                return true;
            }
            return false;
        });

        mainSeekBar = findViewById(R.id.seekBar);

        mMediaPlayer.setOnPreparedListener(mp -> {
            mainSeekBar.setMax(mMediaPlayer.getDuration());

            Log.d("DEBUG", "Duration: " + mMediaPlayer.getDuration());

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    if (mMediaPlayer != null) {
                        mainSeekBar.setProgress(mMediaPlayer.getCurrentPosition(), true);
                    }

                }
            }, 0, 1000);
        });

        mainSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    }

    public void updateSeekBars(){

        // Prevent multiple timers
        if(timer!=null){
            return;
        }

        timer = new Timer();

        // Runs every 500 ms
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                // Runs on main thread instead of background
                runOnUiThread(() -> {
                    if(mMediaPlayer != null && (mMediaPlayer.isPlaying())){
                        int position = mMediaPlayer.getCurrentPosition();

                        // Sets all SeekBar progress to current audio position
                        for(SeekBar seekBar : activeSeekBars){
                            seekBar.setProgress(position);
                        }
                    }
                });

            }
        }, 0, 500);
    }

    public void registerSeekBar(SeekBar seekBar){
        if(!activeSeekBars.contains(seekBar)){
            activeSeekBars.add(seekBar);
            seekBar.setMax(mMediaPlayer.getDuration());
        }
    }

    public void removeSeekBar(SeekBar seekBar){
        activeSeekBars.remove(seekBar);
    }

    public void playIndividualCardSound(Card card, String type, SeekBar chosenSeekBar){

        for(SeekBar seekBar : activeSeekBars){
            seekBar.setVisibility(View.GONE);
        }

        SeekBar currentSeekBar = chosenSeekBar;

        currentSeekBar.setVisibility(View.VISIBLE);

        registerSeekBar(currentSeekBar);

        int sound = card.getmSound();

        playSound("android.resource://" + getPackageName() + "/" + sound, 100);
        Log.d("MediaPlayerDebug", "Resource Name: " + getResources().getResourceName(sound));
    }



    public void playSound(String mp3, int volume){

        if (mMediaPlayer != null && (mMediaPlayer.isPlaying())){
            try {

                //If MediaPlayer already playing, reset and set it uo

                mMediaPlayer.reset();

                mMediaPlayer.setVolume(volume, volume);
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                Log.d("Sound URI", String.valueOf(Uri.parse(mp3)));

                mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mp3));

                mMediaPlayer.prepare();



                mMediaPlayer.start();




            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Catching", Toast.LENGTH_SHORT).show();
            }


        } else {

            try {

                //If MediaPlayer is not initialized or playing, set it up
                mMediaPlayer.setVolume(volume, volume);
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mp3));

                //Didn't work after using prepareAsync
                mMediaPlayer.prepare();



                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        try {
            // Always release the previous instance if it exists
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

            // Create a fresh MediaPlayer
            mMediaPlayer = new MediaPlayer();

            // Set volume and system audio
            mMediaPlayer.setVolume(volume, volume);
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_MUSIC,
                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            // Set data source and prepare
            Uri soundUri = Uri.parse(mp3);
            Log.d("Sound URI", String.valueOf(soundUri));
            mMediaPlayer.setDataSource(getApplicationContext(), soundUri);
            mMediaPlayer.prepare(); // Or use prepareAsync + listener
            mMediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error playing sound", Toast.LENGTH_SHORT).show();
        }*/


    }


    public void getSpeechInput(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        startActivityForResult(intent, 10);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 10);
        }
        else{
            Toast.makeText(this, "Your phone can't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<Card> filteredSamplesByName(String search, ArrayList<Card> samples){
        ArrayList<Card> newSamples = new ArrayList<>();

        for (Card sample : samples){
            Log.d("Title", sample.getLine1());
            String title = sample.getLine1();

            /*if (title.toLowerCase().contains(search.toLowerCase())){

                newSamples.add(sample);
            }*/

            //Create an array of words from the title using .split(), to check if each word starts with the first letter of the search query
            if(title.toLowerCase().startsWith(search)){
                newSamples.add(sample);
            }
            else{
                String[] words = title.split(" ");
                for (String word : words){
                    if(word.toLowerCase().startsWith(search)){

                        //Makes sure the same sample doesn't show up twice in search results
                        if(!newSamples.contains(sample)){
                            newSamples.add(sample);
                        }

                    }
                }

            }
        }
        return newSamples;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                shownSamples = filteredSamplesByName(s, samples);
                adapter = new Adapter(shownSamples, true);

                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(listener);

                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem menuItem) {

                return true;
                //true allows you to open searchview
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                adapter = new Adapter(samples, true);

                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(listener);

                searchView.setQuery("", true);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.println("Result: " + result.get(0));
                }
                break;
        }
    }*/

}
