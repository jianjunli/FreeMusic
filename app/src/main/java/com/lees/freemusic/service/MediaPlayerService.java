package com.lees.freemusic.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.lees.freemusic.IMediaPlayerService;
import com.lees.freemusic.R;
import com.lees.freemusic.activity.CustomMediaPlayer;
import com.lees.freemusic.bean.MediaItem;
import com.lees.freemusic.constant.UrlContants;
import com.lees.freemusic.util.HttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lees on 2017/6/5.
 */

public class MediaPlayerService extends Service {

    /**
     * 系统播放器
     */
    private MediaPlayer mediaPlayer;

    /**
     * media 资源列表
     */
    private List<MediaItem> mediaItems;
    /**
     * 播放的位置
     */
    private int position;

    /**
     * 当前播放的音乐
     */
    private MediaItem mediaItem;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IMediaPlayerService.Stub stub = new IMediaPlayerService.Stub() {

        MediaPlayerService service = MediaPlayerService.this;

        @Override
        public void openAudio(List<MediaItem> mediaItemList, int position) throws RemoteException {
            service.openAudio(mediaItemList, position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {
            service.setPlayMode(playmode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return service.getAudioSessionId();
        }
    };

    /**
     * 打开音乐
     *
     * @param pos
     */
    private void openAudio(List<MediaItem> mediaItemList, int pos) {

        if (mediaItemList != null && mediaItemList.size() > 0) {
            mediaItems = mediaItemList;
            position = pos;

            mediaItem = mediaItems.get(position);
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setDataSource(mediaItem.getData());

                mediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private NotificationManager manager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void start() {
        mediaPlayer.start();

        //当播放歌曲的时候，在状态显示正在播放，点击的时候，可以进入音乐播放页面
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //最主要
        Intent intent = new Intent(this, CustomMediaPlayer.class);
        intent.putExtra("notification", true);//标识来自状态拦
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.music_logo)
                .setContentTitle("Free Music")
                .setContentText("正在播放: " + getName())
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1, notification);

    }

    private void pause() {
        mediaPlayer.pause();
        manager.cancel(1);
    }

    private void stop() {
        mediaPlayer.stop();
    }

    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    private int getDuration() {
        return (int) mediaItem.getDuration();
    }

    private String getArtist() {
        if (mediaItem != null) {
            return mediaItem.getArtist();
        }
        return null;
    }

    private String getName() {
        if (mediaItem != null) {
            return mediaItem.getName();
        }
        return null;
    }

    private String getAudioPath() {
        if (mediaItem != null) {
            return mediaItem.getData();
        }
        return null;
    }

    private void next() {
        position = position + 1;
        if (position > mediaItems.size()-1) {
            position = 0;
        }
        openAudio(mediaItems, position);
    }

    private void pre() {
    }

    private void setPlayMode(int playmode) {
    }

    private int getPlayMode() {
        return 0;
    }

    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private int getAudioSessionId() {
        return 0;
    }

    private class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {

            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }
}
