package com.anshu.allinonemusicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.anshu.allinonemusicplayer.Activity.Adapter.MusicFileAdapter;
import com.anshu.allinonemusicplayer.Activity.Model.MusicModel;
import com.anshu.allinonemusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MusicModel> audioList = new ArrayList<>();
    RecyclerView recyclerView;
    MusicFileAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AllIniCliZeID();
        getAllMusic();


    }

    private void getAllMusic() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MusicFileAdapter(this,audioList);
        recyclerView.setAdapter(adapter);

        //To Get All Audio Files from device

        ContentResolver contentResolver = getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.DURATION};
        Cursor cursor = contentResolver.query(audioUri,projection,null,null,null);

        if (cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));


                MusicModel audioModel = new MusicModel(title,path,album,duration);
                audioList.add(audioModel);
            }
        }

    }

    private void AllIniCliZeID() {
        recyclerView = findViewById(R.id.recyclerView);
    }

}