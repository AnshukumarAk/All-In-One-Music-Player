package com.anshu.allinonemusicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anshu.allinonemusicplayer.Activity.Model.MusicModel;
import com.anshu.allinonemusicplayer.R;
import com.anshu.allinonemusicplayer.databinding.ActivityMusicPlayBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MusicPlayActivity extends AppCompatActivity {


    String MusicName = "", Time = "";
    MediaPlayer mediaPlayer;
    MusicModel audio;
    SeekBar seekBar;
    String Audio_Path="",Audio_Title="",Duration="";
    ActivityMusicPlayBinding binding;

    Handler handler;
    Runnable runnable;
    private boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMusicPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AllIniclizeID();

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            Audio_Path = bundle.getString("Audio_path", "");
            Audio_Title = bundle.getString("Audio_Title", "");
            Duration = bundle.getString("Duration", "");

            if (!Audio_Title.equals("")){
                binding.audioName.setText(Audio_Title);
            }
            if (!Duration.equals("")){
//                binding.txtSongTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(Duration)),
//                        TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(Duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(Duration)))));
                binding.txtSongTime.setText(convertIntoTime(Integer.parseInt(Duration)));
            }
        }

        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Audio_Path.equals("")){
                    if (isPlaying) {
                        if (mediaPlayer.isPlaying()) {

                            Toast.makeText(MusicPlayActivity.this, "Audio is Already Playing", Toast.LENGTH_SHORT).show();
                        } else {
                            playAudio(Audio_Path);
                        }
                    }else {
                        // If not playing, start or resume playback
                        mediaPlayer.start();
                        isPlaying = true;
                    }
                   seekBar.setMax(mediaPlayer.getDuration());
                  handler.postDelayed(runnable,0);
                }



            }
        });
        binding.btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    // If playing, stop playback
                    mediaPlayer.pause();
                    isPlaying = false;
                }
            }
        });


        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };

        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
//        playerdur.setText(sDuration);

        try {
            mediaPlayer.setDataSource(Audio_Path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                }
//                playerpos.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
            }
        });

        // Fetch album art
        Bitmap albumArt = getAlbumArt(Audio_Path);
        if (albumArt != null) {
            binding.imgLogo.setImageBitmap(albumArt);
        } else {
            // Set a default image if no album art found
           binding.imgLogo.setImageResource(R.drawable.music_icon);
        }


    }

    private void PauseAudio() {
        mediaPlayer.stop();
    }

    private void AllIniclizeID() {
        mediaPlayer = new MediaPlayer();
        seekBar=findViewById(R.id.seekBar);
        handler = new Handler();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        finish();
    }



    private void playAudio(String audioPath) {

        ProgressDialog progressDialog = ProgressDialog.show(this,
                "Buffering", "Audio file buffering from URL");
        String audioUrl = audioPath;

        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl);
            // below line is use to prepare
            // and start our media player.
            //mediaPlayer.prepare();
            mediaPlayer.prepareAsync();
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.setLooping(false);
            //seekbar.setMax(mediaPlayer.getDuration());
            //the callback gets called when prepareAsync audio file become ready,
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();

// cancel the dialog
                    progressDialog.cancel();
                }
            });
            //mediaPlayer.start();
            new Thread().start();
//            isPLAYING = true;
//            imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_stop));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // below line is use to display a toast message.
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }

//    private void playNextVideo() {
//        // Check if there is another video to play
//        if (position < path.length() - 1) {
//            position++;
//            if (videosize >position) {
//                playVideo(position);
//            }else {
//                onBackPressed();
//            }
//        } else {
//            // All videos in the playlist are played
//            // You may choose to loop or perform any other action here
//        }
//    }

    private String convertIntoTime(int ms){
        String time;
        int x, seconds, minutes, hours;
        x = ms / 1000;
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;
        if (hours != 0)
            time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        else time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        return time;
    }
    public static Bitmap getAlbumArt(String audioFilePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(audioFilePath);

        byte[] albumArt = retriever.getEmbeddedPicture();
        if (albumArt != null) {
            return BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
        } else {
            return null;
        }
    }

}
