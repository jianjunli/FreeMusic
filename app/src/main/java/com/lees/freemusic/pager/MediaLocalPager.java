package com.lees.freemusic.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.lees.freemusic.R;
import com.lees.freemusic.base.BasePager;


/**
 * Created by lees on 2017/5/23.
 */

public class MediaLocalPager extends BasePager {

    private ListView listView;
    private String[] data;

    public MediaLocalPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.activity_media_list, null);
        listView = (ListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void initData() {


    }
}
