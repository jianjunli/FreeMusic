package com.lees.freemusic;

import android.app.Application;

import org.xutils.x;

/**
 * Created by lees on 2017/6/2.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
