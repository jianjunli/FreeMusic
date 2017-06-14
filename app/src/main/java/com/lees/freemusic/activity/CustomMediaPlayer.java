package com.lees.freemusic.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lees.freemusic.IMediaPlayerService;
import com.lees.freemusic.R;
import com.lees.freemusic.bean.MediaItem;
import com.lees.freemusic.service.MediaPlayerService;
import com.lees.freemusic.util.CommUtils;
import com.lees.freemusic.util.HttpUtils;
import com.lees.freemusic.view.LrcView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 播放器及音乐列表
 * <p>
 * Created by lees on 2017/6/2.
 */

public class CustomMediaPlayer extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 1;

    private Button btn_pre;
    private Button btn_next;
    private Button btn_start_pause;
    private SeekBar seekbar;

    private TextView time_duration;
    private TextView time_progress;

    private TextView song_name;
    private TextView artist;

    private LinearLayout ll_player;

    private LrcView lrcView;


    private IMediaPlayerService service;


    private ArrayList<MediaItem> mediaItems;
    private int position;


    private CommUtils commUtils;

    Handler handler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:

                    try {
                        seekbar.setProgress(service.getCurrentPosition());
                        time_progress.setText(commUtils.stringForTime(service.getCurrentPosition()));
                        // asyncSomethings();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    lrcView.updateTime(seekbar.getProgress());

                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
            }
        }
    };
    private int screenWidth = 0;
    private int screenHeight = 0;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        btn_pre = (Button) findViewById(R.id.btn_pre);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_start_pause = (Button) findViewById(R.id.btn_start_pause);
        song_name = (TextView) findViewById(R.id.song_name);
        artist = (TextView) findViewById(R.id.artist);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        time_duration = (TextView) findViewById(R.id.time_duration);
        time_progress = (TextView) findViewById(R.id.time_progress);
        ll_player = (LinearLayout) findViewById(R.id.ll_player);

        lrcView = (LrcView) findViewById(R.id.lrcView);

        // 得到屏幕的宽和高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        btn_start_pause.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

        commUtils = new CommUtils();

        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("mediaItems");

        if (mediaItems != null && mediaItems.size() > 0) {
            bindAndStartService();
        }


        EventBus.getDefault().register(this);
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MediaPlayerService.class);
        //intent.setAction("com.atguigu.mobileplayer_OPENAUDIO");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startService(intent);//不至于实例化多个服务
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMediaPlayerService.Stub.asInterface(iBinder);

            try {
                service.openAudio(mediaItems, position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            try {
                if (service != null) {
                    service.stop();
                    service = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    class MyReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {

            showData(null);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showData(MediaItem mediaItem) {
        try {
            song_name.setText(service.getName());
            artist.setText(service.getArtist());
            seekbar.setMax(service.getDuration());
            seekbar.setProgress(service.getCurrentPosition());

            time_duration.setText(commUtils.stringForTime(service.getDuration()));
            time_progress.setText(commUtils.stringForTime(service.getCurrentPosition()));

            x.image().loadDrawable(mediaItem.getImgUrl(), null, new Callback.CommonCallback<Drawable>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onSuccess(Drawable result) {
                    ll_player.setBackground(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

            loadLrc(mediaItem.getLrcUrl());

            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        if (service != null) {
            unbindService(connection);
            connection = null;
        }

        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_pause:
                changePlayPause();
                break;
            case R.id.btn_next:
                next();
                break;
        }
    }

    private void next() {
        try {
            service.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void changePlayPause() {
        try {
            if (service != null) {
                if (service.isPlaying()) {
                    service.pause();
                    btn_start_pause.setBackgroundResource(R.drawable.btn_play);
                } else {
                    service.start();
                    btn_start_pause.setBackgroundResource(R.drawable.btn_pause);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    service.seekTo(progress);
                    lrcView.onDrag(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    private void loadLrc(final String path) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder builder = new StringBuilder();
                try {
                    URL url = new URL(params[0]);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String s;
                    while ((s = reader.readLine()) != null) {
                        builder.append(s).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return builder.toString();
            }

            @Override
            protected void onPostExecute(String aVoid) {
                lrcView.loadLrc(aVoid);
            }
        }.execute(path);
    }
}
