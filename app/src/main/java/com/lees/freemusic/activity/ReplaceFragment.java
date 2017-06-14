package com.lees.freemusic.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lees.freemusic.base.BasePager;


/**
 * Created by lees on 2017/5/23.
 */

public class ReplaceFragment extends Fragment {

    private BasePager currentPager;

    public ReplaceFragment(BasePager currentPager) {
        this.currentPager = currentPager;
        getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return currentPager.view;
    }
}
