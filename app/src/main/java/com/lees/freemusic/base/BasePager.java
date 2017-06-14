package com.lees.freemusic.base;

import android.content.Context;
import android.view.View;

/**
 * Created by lees on 2017/5/23.
 */

public abstract class BasePager {

    public Context context;

    public View view;

    public BasePager(Context context) {
        this.context = context;
        view = initView();
    }

    public abstract View initView();


    public void initData() {
        // 需要初始化数据的来实现
    }
}
