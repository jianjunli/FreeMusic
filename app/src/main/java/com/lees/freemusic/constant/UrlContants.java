package com.lees.freemusic.constant;

/**
 * 音乐Api地址
 * <p>
 * Created by lees on 2017/6/2.
 */

public class UrlContants {

    /**
     * 获取方式：GET
     * 参数：
     * format: json|xml
     * callback:
     * from: webapp_music
     * method:
     */
    public static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";


    /**
     * 获取列表
     * type: //1、新歌榜，2、热歌榜，11、摇滚榜，12、爵士，16、流行 21、欧美金曲榜，22、经典老歌榜，23、情歌对唱榜，24、影视金曲榜，25、网络歌曲榜
     * size: 10 //返回条目数量
     * offset: 0 //获取偏移
     */
    public static final String LIST_URL = BASE_URL + "?offset=0&size=10&type=%s&_t=1468380543284&format=json&method=baidu.ting.billboard.billList";

    /**
     * 搜索
     */
    public static final String QUERY_URL = BASE_URL + "?method=baidu.ting.search.catalogSug&query={}";

    /**
     * 播放（获取MP3地址）
     */
    public static final String PLAY_URL = BASE_URL + "?method=baidu.ting.song.play&songid=%s";

    /**
     * 播放（获取MP3地址）
     */
    public static final String PLAY_AAC_URL = BASE_URL + "?method=baidu.ting.song.playAAC&songid={}";

    /**
     * 获取LRC 歌词
     */
    public static final String LRC_URL = BASE_URL + "?method=baidu.ting.song.lry&songid={}";

    /**
     * 推荐列表
     * num：推荐条数
     */
    public static final String RECOMMAND_URL = BASE_URL + "?method=baidu.ting.song.getRecommandSongList&songid={}&num=5";


    /**
     * 下载
     * songid: //歌曲id
     * bit: //码率（24, 64, 128, 192, 256, 320, flac）
     * _t: //时间戳（当前时间）
     */
    public static final String DOWNLOAD_URL =BASE_URL + "?method=baidu.ting.song.downWeb&songid={}&bit={}&_t={}";


    /**
     * 获取歌手歌曲列表
     * tinguid: //歌手ting id
     * limits: //返回条目数量
     */
    public static final String SONG_LIST_URL = BASE_URL +  "?method=baidu.ting.artist.getSongList&tinguid={}&limits={}&use_cluster=1&order=2";

    /**
     * 获取歌手信息
     * tinguid: //歌手ting id
     * limits: //返回条目数量
     */
    public static final String INFO_URL = BASE_URL +  "?method=baidu.ting.artist.getInfo&tinguid={}";


    public static final String TEST_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?size=40&type=2&_t=1468380543284&format=json&method=baidu.ting.billboard.billList";


    //public static final String TEST_URL = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

}
