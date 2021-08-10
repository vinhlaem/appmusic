package com.example.appmusic.ServiceMp3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.datasource.DataSource;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appmusic.Activity.DanhhsachbaihatActivity;
import com.example.appmusic.Adapter.PlayListAdapter;
import com.example.appmusic.ConectionApi.APIService;
import com.example.appmusic.ConectionApi.Dataservice;
import com.example.appmusic.Model.Baihat;
import com.example.appmusic.Model.Playlist;
import com.example.appmusic.R;
import com.example.appmusic.Util.Constants;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerMp3 extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private String listMp3 = "";
    private MediaPlayer mediaPlayer;
    private ArrayList<Baihat> arrMusic;
    TextView txttotaltimesong;
    SeekBar sktime;



    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(listenActionControl,
                new IntentFilter(Constants.ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Ham Gia tri tra ve
        listMp3 = intent.getStringExtra(Constants.LIST_MP3);
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.release();
        }
        initMedia(listMp3);
        actionControlPlayPaueSevice(this.ACCESSIBILITY_SERVICE);
        return START_NOT_STICKY;


    }

//    public PlayerMp3(ArrayList<Baihat> arrMusic) {
//        this.arrMusic = arrMusic;
//        Log.d("BBB",arrMusic.get(0).getLinkBaiHat());
//    }


    private void initMedia(final String linksMp3) {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(linksMp3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            //timesong();

            }
        });

    }

    //private void timesong() {
      //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        //txttotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        //sktime.setMax(mediaPlayer.getDuration());
    //}

    @Override
    public void onPrepared(MediaPlayer mp) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy", "onDestroy");
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listenActionControl);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    private BroadcastReceiver listenActionControl = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Constants.ACTION);
            switch (action) {
                case Constants.PLAY_PAUSE:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();

                    } else {
                        mediaPlayer.start();
                    }
                    break;
                case Constants.NEXT_SONG:
                    Toast.makeText(PlayerMp3.this, "next song!", Toast.LENGTH_LONG).show();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        mediaPlayer.release();
                        listMp3 = intent.getStringExtra(Constants.LINKS_MP3);
                        initMedia(listMp3);
                    }
                    break;
                case Constants.PRE_SONG:
                    Toast.makeText(PlayerMp3.this, "pre song!", Toast.LENGTH_LONG).show();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        mediaPlayer.release();
                        listMp3 = intent.getStringExtra(Constants.LINKS_MP3);
                        initMedia(listMp3);
                    }
                    break;
            }
        }
    };
    private void actionControlPlayPaueSevice (String action){
        Intent intent = new Intent(Constants.ACTION);
        intent.putExtra(Constants.ACTION, action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
//
//    private void pushNotification() {
//
//        String CHANNEL_ID = "CHANNEL_ID";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID, CHANNEL_ID,
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager =
//                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            manager.createNotificationChannel(channel);
//        }
//
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
//
//
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this, CHANNEL_ID);
//        builder.setSmallIcon(R.drawable.noti_dianhac);
//        builder.setCustomBigContentView(remoteViews);
//        startForeground(1213232, builder.build());
//    }

    private void pushNotification() {
        Intent intent = new Intent(this, getClass());
        startService(intent);

        String CHANNEL_ID = "CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
//        remoteViews.setTextViewText(R.id.txt_categoryMusic_notifi, arrMusic.get(currentIndex).getCatagory());
//        remoteViews.setTextViewText(R.id.txt_nameMusic_notifi, arrMusic.get(currentIndex).getTitle());
//        if (player.isPlaying()) {
//            remoteViews.setImageViewResource(R.id.img_notifi_player, R.drawable.ic_pause_gray);
//        } else {
//            remoteViews.setImageViewResource(R.id.img_notifi_player, R.drawable.ic_play_gray);
//        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setCustomBigContentView(remoteViews);
        startForeground(1213232, builder.build());
    }
    void showCover(final RemoteViews remoteView, Uri imageUri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(imageUri)
//                .setRequestPriority(Picasso.Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage(imageRequest, this);
        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (bitmap == null) {
                       // remoteView.setImageViewResource(R.id.image_view_album, R.drawable.ic_home);
                        return;
                    }
                    //remoteView.setImageViewBitmap(R.id.image_view_album, bitmap);
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here
                }
            }, CallerThreadExecutor.getInstance());
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.start();
        return false;
    }
}
