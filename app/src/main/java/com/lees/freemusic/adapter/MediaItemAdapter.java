package com.lees.freemusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lees.freemusic.R;
import com.lees.freemusic.bean.MediaItem;

import java.util.ArrayList;

/**
 * Created by lees on 2017/6/2.
 */

public class MediaItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MediaItem> mediaList;

    public MediaItemAdapter(Context context, ArrayList<MediaItem> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }


    @Override
    public int getCount() {
        return mediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_music, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MediaItem item = mediaList.get(position);

        viewHolder.tv_size.setText(item.getName());


        return convertView;
    }

    static class ViewHolder {

        TextView tv_size;
    }
}
