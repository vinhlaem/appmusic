package com.example.appmusic.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appmusic.R;

public class Fragment_Nhaccanhan extends Fragment {
    View view;
    TextView txtname;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nhaccanhan, container, false);
        txtname = view.findViewById(R.id.txtname);
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        txtname.setText(preferences.getString("username",""));
        return view;

    }
}
