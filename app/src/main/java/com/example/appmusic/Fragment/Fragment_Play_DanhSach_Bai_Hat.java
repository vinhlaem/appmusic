package com.example.appmusic.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.Activity.PlayNhacActivity;
import com.example.appmusic.Adapter.PlaynhacAdapter;
import com.example.appmusic.Model.Baihat;
import com.example.appmusic.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Play_DanhSach_Bai_Hat extends Fragment {
    View view;
    RecyclerView recyclerViewPlaynhac;
    PlaynhacAdapter playnhacAdapter;
    private List<Baihat> mangbaihat = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danh_sach_cac_bai_hat,container,false);
        recyclerViewPlaynhac = view.findViewById(R.id.recycerlerviewplaybaihat);
        if (PlayNhacActivity.mangbaihat.size() > 0){
            playnhacAdapter = new PlaynhacAdapter(getActivity(), (ArrayList<Baihat>) PlayNhacActivity.mangbaihat);
            recyclerViewPlaynhac.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewPlaynhac.setAdapter(playnhacAdapter);

        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayNhacActivity.mangbaihat.clear();
    }
}
