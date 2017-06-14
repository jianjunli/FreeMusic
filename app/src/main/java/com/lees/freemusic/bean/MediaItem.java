package com.lees.freemusic.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by lees on 2017/6/3.
 */

public class MediaItem implements Parcelable {

    /**
     * 歌曲Id
     */
    private String songId;

    /**
     * 歌曲名称
     */
    private String name;

    /**
     * 歌曲时长
     */
    private long duration;

    /**
     * 歌曲大小
     */
    private long size;

    /**
     * 艺术家（歌手）
     */
    private String artist;

    /**
     * 资源绝对地址
     */
    private String data;

    private String lrcUrl;

    private String imgUrl;

    public MediaItem(){}


    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "songId='" + songId + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", artist='" + artist + '\'' +
                ", data='" + data + '\'' +
                ", lrcUrl='" + lrcUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songId);
        dest.writeString(name);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(artist);
        dest.writeString(data);
        dest.writeString(lrcUrl);
        dest.writeString(imgUrl);
    }


    public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator<MediaItem>() {

        @Override
        public MediaItem createFromParcel(Parcel source) {
            MediaItem item = new MediaItem();
            item.songId = source.readString();
            item.name = source.readString();
            item.duration = source.readLong();
            item.size = source.readLong();
            item.artist = source.readString();
            item.data = source.readString();
            item.lrcUrl = source.readString();
            item.imgUrl = source.readString();
            return item;
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };
}
