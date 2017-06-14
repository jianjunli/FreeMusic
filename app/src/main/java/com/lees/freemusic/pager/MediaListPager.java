package com.lees.freemusic.pager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lees.freemusic.R;
import com.lees.freemusic.activity.CustomMediaPlayer;
import com.lees.freemusic.adapter.MediaItemAdapter;
import com.lees.freemusic.adapter.MediaListAdapter;
import com.lees.freemusic.base.BasePager;
import com.lees.freemusic.bean.MediaItem;
import com.lees.freemusic.bean.MediaList;
import com.lees.freemusic.constant.UrlContants;
import com.lees.freemusic.util.HttpUtils;
import com.lees.freemusic.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lees on 2017/5/23.
 */

public class MediaListPager extends BasePager {

    private ListView listView;
    private ProgressBar pb_loading;


    private ArrayList<MediaList> mediaLists;

    private MediaListAdapter listAdapter;

    private MediaItemAdapter itemAdapter;

    private ArrayList<MediaItem> mediaItems;

    private Map<String, String> tmpResult;


    public MediaListPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.activity_media_list, null);
        listView = (ListView) view.findViewById(R.id.listview);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);

        listView.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    @Override
    public void initData() {
        initMediaList();
        listAdapter = new MediaListAdapter(context, mediaLists);
        listView.setAdapter(listAdapter);
    }


    private void initMediaList() {
        mediaLists = new ArrayList<>();
        mediaLists.add(new MediaList(1, "新歌榜"));
        mediaLists.add(new MediaList(2, "热歌榜"));

        mediaLists.add(new MediaList(11, "摇滚榜"));
        mediaLists.add(new MediaList(12, "爵士"));
        mediaLists.add(new MediaList(16, "流行"));
        mediaLists.add(new MediaList(21, "欧美金曲榜"));
        mediaLists.add(new MediaList(22, "经典老歌榜"));
        mediaLists.add(new MediaList(23, "情歌对唱榜"));
        mediaLists.add(new MediaList(24, "影视金曲榜"));
        mediaLists.add(new MediaList(25, "网络歌曲榜"));
    }

    private void proccList(final String result) {


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.optJSONArray("song_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String title = object.optString("title");
                String artist = object.optString("artist_name");
                String songId = object.optString("song_id");

                MediaItem item = new MediaItem();
                item.setName(title);
                item.setArtist(artist);
                item.setSongId(songId);
                mediaItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String proccOne(final String result, String root, String key) {

        String fileLink = "";

        try {
            JSONObject jsonObject = new JSONObject(result);
            fileLink = jsonObject.optJSONObject(root).optString(key);
            return fileLink;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fileLink;
    }







    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            tmpResult = new HashMap<>();
            mediaItems = new ArrayList<>();

            MediaList mediaList = mediaLists.get(position);
            final String url = String.format(UrlContants.LIST_URL, mediaList.getSongType());

            new AsyncTask<String, Void, String>() {

                @Override
                protected void onPreExecute() {
                    pb_loading.setVisibility(View.VISIBLE);
                }

                @Override
                protected String doInBackground(String... params) {
                    return HttpUtils.fetchDataByGet(params[0]);
                }

                @Override
                protected void onPostExecute(String result) {
                    proccList(result);
                    getSongInfo();

                }
            }.execute(url);



        }
    }


    private void getSongInfo() {

        new AsyncTask<ArrayList<MediaItem>, Void, Void>() {

            @Override
            protected void onPreExecute() {
                pb_loading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(ArrayList<MediaItem>... params) {
                ArrayList<MediaItem> items = params[0];
                for (MediaItem item : items) {
                    String url = String.format(UrlContants.PLAY_URL, item.getSongId());
                    String result = HttpUtils.fetchDataByGet(url);
                    item.setData(proccOne(result, "bitrate", "file_link"));
                    item.setLrcUrl(proccOne(result, "songinfo", "lrclink"));
                    item.setDuration(Long.valueOf(proccOne(result, "bitrate", "file_duration")) * 1000);
                    item.setImgUrl(proccOne(result, "songinfo", "pic_big"));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                pb_loading.setVisibility(View.GONE);

                toPlay();

            }
        }.execute(mediaItems);
    }

    private void toPlay() {
        Intent intent = new Intent(context, CustomMediaPlayer.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mediaItems", mediaItems);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        for (MediaItem item:
             mediaItems) {
            Log.i("TAG", item.getSongId() + "," + item.getName() + "," + item.getData());
        }

        context.startActivity(intent);
    }
}
