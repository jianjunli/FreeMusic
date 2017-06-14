package com.lees.freemusic.bean;

import java.io.Serializable;

/**
 * Created by lees on 2017/6/3.
 */

public class MediaList implements Serializable {

    private int songType;

    private String songDesc;

    private String desc;

    public MediaList() {
    }

    public MediaList(int songType, String songDesc) {
        this.songType = songType;
        this.songDesc = songDesc;
    }

    public int getSongType() {
        return songType;
    }

    public void setSongType(int songType) {
        this.songType = songType;
    }

    public String getSongDesc() {
        return songDesc;
    }

    public void setSongDesc(String songDesc) {
        this.songDesc = songDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
