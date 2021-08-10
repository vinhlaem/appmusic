package com.example.appmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appmusic.Model.Baihat;
import com.example.appmusic.Model.Playlist;
import com.example.appmusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends ArrayAdapter<Playlist> {
    Context context;
    ArrayList<Baihat> mangbaihat;



    public PlayListAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
        super(context, resource, objects);

    }
    class ViewHoldder{
        TextView txtviewplaylist;
        ImageView imgbackground,imgplaylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHoldder viewHoldder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.dong_playlist,null);
            viewHoldder = new ViewHoldder();
            viewHoldder.txtviewplaylist = convertView.findViewById(R.id.textviewtenplaylist);
            viewHoldder.imgplaylist = convertView.findViewById(R.id.imageviewplaylist);
            viewHoldder.imgbackground = convertView.findViewById(R.id.imageviewbackgroundplaylist);
            convertView.setTag(viewHoldder);
        }
        else {
            viewHoldder = (ViewHoldder) convertView.getTag();
        }


        Playlist playlist = getItem(position);
        Picasso.with(getContext()).load(playlist.getHinhPlaylist()).into(viewHoldder.imgbackground);
        Picasso.with(getContext()).load(playlist.getIcon()).into(viewHoldder.imgplaylist);
        viewHoldder.txtviewplaylist.setText(playlist.getTen());
        return convertView;
    }

}
