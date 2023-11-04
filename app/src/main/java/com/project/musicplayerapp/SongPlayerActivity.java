package com.project.musicplayerapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import android.hardware.SensorEventListener;


public class SongPlayerActivity extends AppCompatActivity implements SensorEventListener{
    private MediaPlayer player;
    SeekBar seekBar;
    ImageButton play_or_pause, favourite, nextSong, previousSong;
    TextView songTitle, songArtist;
    TextView playedDuration, totalDuration;
    private ArrayList<Allsongs> allsongsArrayList;
    private ArrayList<Allsongs> favouritesList;
    private int currentSongIndex;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private long lastShakeTime;
    private static final long SHAKE_INTERVAL = 1000; // Minimum time between two shakes (in milliseconds)
    private PowerManager.WakeLock wakeLock;
    Handler handler = new Handler();

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (player != null) {
                int mCurrentPosition = player.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);
                playedDuration.setText(getTimeString(mCurrentPosition));
            }
            handler.postDelayed(this, 1000); // Update every second
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_playing_activity);
        //getting data from intent
        String songName = getIntent().getStringExtra("SONG_NAME");
        String artistName = getIntent().getStringExtra("ARTIST_NAME");
        int songResourceId = getIntent().getIntExtra("SONG_RESOURCE_ID",-1);

        player = MediaPlayer.create(this, songResourceId);

        seekBar = findViewById(R.id.seekBar);
        play_or_pause = findViewById(R.id.playPauseButton);
        favourite = findViewById(R.id.favoriteIcon);
        nextSong = findViewById(R.id.nextButton);
        previousSong = findViewById(R.id.previousButton);
        songTitle = findViewById(R.id.songTitle);
        songArtist = findViewById(R.id.songArtist);
        playedDuration = findViewById(R.id.startTime);
        totalDuration = findViewById(R.id.endTime);

        songTitle.setText(songName);
        songArtist.setText(artistName);

        seekBar.setMax(player.getDuration());
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong(); // Play the next song when the current song finishes
            }
        });
        player.start();

        // Initialize the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Acquire a wake lock to keep the screen on during song changes
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MusicPlayerApp::ShakeWakeLock");
            wakeLock.acquire();
        }

        allsongsArrayList = new ArrayList<>();
        String[] songname = new String[]{
                getString(R.string.song_1),
                getString(R.string.song_2),
                getString(R.string.song_3),
                getString(R.string.song_4),
                getString(R.string.song_5),
                getString(R.string.song_6),
                getString(R.string.song_7),
                getString(R.string.song_8),
        };
        String[] artistname = new String[]{
                getString(R.string.artist_1),
                getString(R.string.artist_2),
                getString(R.string.artist_3),
                getString(R.string.artist_4),
                getString(R.string.artist_5),
                getString(R.string.artist_6),
                getString(R.string.artist_7),
                getString(R.string.artist_8),
        };
        int[] songResourceIds = new int[]{
                R.raw.yellow,
                R.raw.cake_by_the_ocean,
                R.raw.poker_face,
                R.raw.jealous,
                R.raw.night_changes,
                R.raw.radio,
                R.raw.die_for_you,
                R.raw.do_i_wanna_know,
        };
        for(int i=0;i< songname.length;i++){
            Allsongs allsongs=new Allsongs(songname[i],artistname[i],songResourceIds[i]) ;
            allsongsArrayList.add(allsongs);
        }
        favouritesList = new ArrayList<>();

        currentSongIndex = getSelectedSongIndex(songResourceId);

        SongPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    int mCurrentPosition = player.getCurrentPosition();
                    int mTotalDuration = player.getDuration();
                    seekBar.setProgress(mCurrentPosition);
                    playedDuration.setText(getTimeString(mCurrentPosition));
                    totalDuration.setText(getTimeString(mTotalDuration));
                }
                handler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    player.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not used in this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not used in this example
            }
        });

        play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (player.isPlaying()) {
                    player.pause();
                    play_or_pause.setBackgroundResource(R.drawable.play_button);
                } else {
                    player.start();
                    play_or_pause.setBackgroundResource(R.drawable.pause_button);
                }
            }
        });

        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextSong();
            }
        });

        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousSong();
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites();
            }
        });

    }
public static String getTimeString(int currentPositionInMillis) {
        int totalSeconds = currentPositionInMillis / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        // Create the formatted string in minutes:seconds format
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        return formattedTime;
    }
    private void playNextSong() {
        if (currentSongIndex != -1) {
            currentSongIndex++;
            if (currentSongIndex >= allsongsArrayList.size()) {
                currentSongIndex = 0; // Play the first song if we reach the end
            }
            Allsongs nextSong = allsongsArrayList.get(currentSongIndex);
            // Update the song name and artist name TextViews
            songTitle.setText(nextSong.getSongName());
            songArtist.setText(nextSong.getArtistName());

            // Stop and release the current player
            releaseMediaPlayer();

            // Create a new player for the next song
            player = MediaPlayer.create(this, nextSong.getResourceId());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playNextSong(); // Play the next song when the current song finishes
                }
            });
            player.start();
            seekBar.setMax(player.getDuration());
            updateSeekBar.run();
        }
    }

        private void playPreviousSong() {
        if (currentSongIndex != -1) {
            currentSongIndex--;
            if (currentSongIndex < 0) {
                currentSongIndex = allsongsArrayList.size() - 1; // Play the last song if we reach the beginning
            }
            Allsongs previousSong = allsongsArrayList.get(currentSongIndex);
            // Update the song name and artist name TextViews
            songTitle.setText(previousSong.getSongName());
            songArtist.setText(previousSong.getArtistName());

            // Stop and release the current player
            releaseMediaPlayer();

            // Create a new player for the previous song
            player = MediaPlayer.create(this, previousSong.getResourceId());
            player.start();
            seekBar.setMax(player.getDuration());
            updateSeekBar.run();
        }
    }
    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private int getSelectedSongIndex(int songResourceId) {
        for (int i = 0; i < allsongsArrayList.size(); i++) {
            Allsongs song = allsongsArrayList.get(i);
            if (song.getResourceId() == songResourceId) {
                return i;
            }
        }
        return -1; // Song not found
    }
    // Methods for handling favourites
    private void addToFavorites() {
        Allsongs currentSong = allsongsArrayList.get(currentSongIndex);

        // Toggle the favorite state
        boolean isFavorite = !currentSong.isFavorite();
        currentSong.setFavorite(isFavorite);

        // Update the favorite button icon
        updateFavoriteButtonIcon(isFavorite);
        // Update the favorites list
        if (isFavorite) {
            favouritesList.add(currentSong); // Add to favorites list
        } else {
            favouritesList.remove(currentSong); // Remove from favorites list
        }

        // Store the list of favorite songs in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonFavoritesList = gson.toJson(favouritesList);
        editor.putString("FAVORITES_LIST", jsonFavoritesList);
        editor.apply();

        // Update the FavouritesFragment with the latest favorites list
        updateFavouritesFragment();
    }
    private void updateFavoriteButtonIcon(boolean isFavorite) {
        if (isFavorite) {
            favourite.setImageResource(R.drawable.favourite_on); // Set the solid heart image
        } else {
            favourite.setImageResource(R.drawable.favourite_off); // Set the outline heart image
        }
    }
    private void updateFavouritesFragment() {
        // Pass the updated list of favorites to the FavouritesFragment
        FavouritesFragment favouritesFragment = (FavouritesFragment) getSupportFragmentManager().findFragmentByTag("favourites_fragment");
        if (favouritesFragment != null) {
            favouritesFragment.setFavouritesList(favouritesList);
        }
    }
    public void onSensorChanged(SensorEvent event) {
        // Get the accelerometer values
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Calculate acceleration magnitude
        double acceleration = Math.sqrt(x * x + y * y + z * z);

        // Detect shaking based on acceleration threshold
        if (acceleration > 12.0) { // Adjust the threshold as needed
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShakeTime >= SHAKE_INTERVAL) {
                // Change the song when a shake is detected
                playNextSong();
                lastShakeTime = currentTime;
            }
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register accelerometer sensor listener when the activity resumes
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister accelerometer sensor listener when the activity pauses
        sensorManager.unregisterListener(this, accelerometerSensor);
    }

    protected void onDestroy() {
        if (player != null) {
            player.release();
            player = null;
        }
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        super.onDestroy();
    }
}
