package com.lees.freemusic.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lees.freemusic.R;
import com.lees.freemusic.base.BasePager;
import com.lees.freemusic.pager.MediaLikePager;
import com.lees.freemusic.pager.MediaListPager;
import com.lees.freemusic.pager.MediaLocalPager;
import com.lees.freemusic.pager.MediaSingerPager;

import java.util.ArrayList;

/**
 * Created by lees on 2017/6/1.
 */

public class MainActivity extends FragmentActivity{

    private RadioGroup btn_radio_group;

    private ArrayList<BasePager> pagers;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_radio_group = (RadioGroup) findViewById(R.id.btn_radio_group);


        pagers = new ArrayList<>();
        pagers.add(new MediaListPager(this));
        pagers.add(new MediaSingerPager(this));
        pagers.add(new MediaLikePager(this));
        pagers.add(new MediaLocalPager(this));

        btn_radio_group.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        // 默认选中第一个
        ((RadioButton)btn_radio_group.findViewById(R.id.btn_radio)).setChecked(true);
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {

                case R.id.btn_radio:
                    position = 0;
                    break;
                case R.id.btn_singer:
                    position = 1;
                    break;
                case R.id.btn_like:
                    position = 2;
                    break;
                case R.id.btn_local:
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            setFragment();

        }
    }

    private void setFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fl_main, new ReplaceFragment(getBasePager()));

        transaction.commit();

    }


    public BasePager getBasePager() {
        BasePager basePager = pagers.get(position);
        if (basePager != null) {
            basePager.initData();
        }
        return basePager;
    }
}
