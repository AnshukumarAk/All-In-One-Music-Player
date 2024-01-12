package com.anshu.allinonemusicplayer.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anshu.allinonemusicplayer.Activity.Model.MusicModel;
import com.anshu.allinonemusicplayer.Activity.MusicPlayActivity;
import com.anshu.allinonemusicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MusicFileAdapter extends RecyclerView.Adapter<MusicFileAdapter.AudioViewHolder>{

    List<MusicModel> audios = new ArrayList<>();
    Context context;

    public MusicFileAdapter (Context context,List<MusicModel> audios){
        this.context = context;
        this.audios = audios;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_music_file_list,parent,false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        MusicModel audioModel = audios.get(position);
        holder.titleText.setText(audioModel.getTitle());
        holder.pathText.setText(audioModel.getPath());
        String path = audioModel.getPath();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MusicPlayActivity.class);
                intent.putExtra("Audio_path",path);
                intent.putExtra("Audio_Title",audioModel.getTitle());
                intent.putExtra("Duration",audioModel.getDuration());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder{

        TextView titleText,pathText;
        CardView cardView;
        ImageView iv_album;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.title_tv);
            pathText = itemView.findViewById(R.id.path_tv);
            cardView = itemView.findViewById(R.id.card_view);
            iv_album = itemView.findViewById(R.id.iv_album);

        }
    }
}
