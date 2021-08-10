package com.example.appmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;

import com.example.appmusic.Adapter.ViewPagerPlayListNhac;
import com.example.appmusic.ConectionApi.APIService;
import com.example.appmusic.ConectionApi.Dataservice;
import com.example.appmusic.Fragment.Fragment_Dia_Nhac;
import com.example.appmusic.Fragment.Fragment_Play_DanhSach_Bai_Hat;
import com.example.appmusic.Model.Baihat;
import com.example.appmusic.Model.Playlist;
import com.example.appmusic.R;
import com.example.appmusic.ServiceMp3.PlayerMp3;
import com.example.appmusic.Util.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayNhacActivity extends AppCompatActivity{

    private String linksMp3 = "";
    ImageView imageView_back;
    TextView txtTimesong, txtTotaltimesong;
    SeekBar sktimel;
    ImageButton imgplay, imgrepeat, imgnext, imgpre, imgradom;
    ViewPager viewPagerplaynhac;
    public static List<Baihat> mangbaihat = new ArrayList<>();
    public static ViewPagerPlayListNhac adapternhac;
    Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Play_DanhSach_Bai_Hat fragment_play_danhSach_bai_hat;
    MediaPlayer mediaPlayer;
    private ArrayList<Playlist> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetDAtaFromIntent();
        startService();
        init();
        evenClick();
        LocalBroadcastManager.getInstance(this).registerReceiver(listenActionControl,
                new IntentFilter(Constants.ACTION));

    }

    private void startService(){
        Intent intent = new Intent(this, PlayerMp3.class);
        intent.putExtra(Constants.LIST_MP3,linksMp3);
        startService(intent);
    }

    private void evenClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapternhac.getItem(1) != null) {
                    if (mangbaihat.size() > 0) {
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(0).getHinhBaiHat());
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 300);
                    }
                }
            }
        }, 500);
        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionControl(Constants.PLAY_PAUSE);
            }
        });
        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posLinksPm3 = 0;
                for (int i = 0; i<mangbaihat.size(); i++){
                    if (mangbaihat.get(i).getLinkBaiHat().equals(linksMp3)){
                        posLinksPm3 = i;
                        if (posLinksPm3+1 >= mangbaihat.size()){
                            Toast.makeText(PlayNhacActivity.this, "It is Song the end", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            linksMp3 = mangbaihat.get(posLinksPm3+1).getLinkBaiHat();
                            actionControl(Constants.NEXT_SONG);
                        }
                        break;
                    }
                }
            }
        });
        imgpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posLinksmp3 = 0;
                for (int i = 0; i<mangbaihat.size(); i++){
                    if (mangbaihat.get(i).getLinkBaiHat().equals(linksMp3)){
                        posLinksmp3 = i;
                        if (i == 0){
                            Toast.makeText(PlayNhacActivity.this, "It is Song The End", Toast.LENGTH_SHORT).show();
                            return;
                        }else if (posLinksmp3 - 1 >= 0){
                            linksMp3 = mangbaihat.get(posLinksmp3 - 1).getLinkBaiHat();
                            actionControl(Constants.PRE_SONG);
                        }
                    }

                }
            }
        });
    }

    private void GetDAtaFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("cakhuc")) {
                Baihat baihat = intent.getParcelableExtra("cakhuc");
                mangbaihat.add(baihat);
            }
            if (intent.hasExtra("cacbaihat")) {
                ArrayList<Baihat> baihatArrayList = intent.getParcelableArrayListExtra("cacbaihat");
                mangbaihat = baihatArrayList;
            }
            linksMp3 = mangbaihat.get(0).getLinkBaiHat();
        }
    }



    private void init() {
        imageView_back = findViewById(R.id.img_back);
        txtTimesong = findViewById(R.id.textviewtimesong);
        txtTotaltimesong = findViewById(R.id.textviewtotaltimesong);
        sktimel = findViewById(R.id.seekbarsong);
        imgplay = findViewById(R.id.imagebuttonplay);
        imgrepeat = findViewById(R.id.imagebuttonrepeat);
        imgnext = findViewById(R.id.imagebuttonnext);
        imgpre = findViewById(R.id.imagebuttonpre);
        imgradom = findViewById(R.id.imagebuttonsuffle);
        viewPagerplaynhac = findViewById(R.id.viewpaferplaynhac);



        fragment_dia_nhac = new Fragment_Dia_Nhac();
        fragment_play_danhSach_bai_hat = new Fragment_Play_DanhSach_Bai_Hat();
        adapternhac = new ViewPagerPlayListNhac(getSupportFragmentManager());
        adapternhac.AddFragment(fragment_play_danhSach_bai_hat);
        adapternhac.AddFragment(fragment_dia_nhac);
        viewPagerplaynhac.setAdapter(adapternhac);
        fragment_dia_nhac = (Fragment_Dia_Nhac) adapternhac.getItem(1);
        if (mangbaihat.size() > 0) {
//            getSupportActionBar().setTitle(mangbaihat.get(0).getTenBaiHat());
            String linkMp3 = mangbaihat.get(0).getLinkBaiHat();
            imgplay.setImageResource(R.drawable.iconpause);
        }

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


    }


    private void timeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        sktimel.setMax(mediaPlayer.getDuration());
    }

    private void actionControl (String action){
        Intent intent = new Intent(Constants.ACTION);
        intent.putExtra(Constants.ACTION, action);
        intent.putExtra(Constants.LINKS_MP3, linksMp3);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private BroadcastReceiver listenActionControl = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getStringExtra(Constants.ACTION);
            switch (action) {
                case Constants.PLAY_PAUSE:
                    imgplay.setImageResource(R.drawable.iconplay);
                    imgplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgplay.setImageResource(R.drawable.iconpause);
                            if (Constants.PLAY_PAUSE != null){
                                actionControl(action);
                            }else {
                                startService();
                            }
                        }
                    });
                    break;
            }
        }
    };

    class PlayMp3 extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];

        }

        @Override
        protected void onPostExecute(String baihat) {

            super.onPostExecute(baihat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(baihat);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            timeSong();
        }
    }




}