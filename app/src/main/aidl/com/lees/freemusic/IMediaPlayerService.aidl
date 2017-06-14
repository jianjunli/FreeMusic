// IMediaPlayerService.aidl
package com.lees.freemusic;

import com.lees.freemusic.bean.MediaItem;

// Declare any non-default types here with import statements

interface IMediaPlayerService {

         void openAudio(in List<MediaItem> mediaItems, int position);

         void start();

         void pause() ;

         void stop() ;

         int getCurrentPosition() ;

         int getDuration() ;

         String getArtist();

         String getName() ;

         String getAudioPath();

         void next() ;

         void pre() ;

         void setPlayMode(int playmode) ;

         int getPlayMode() ;

         boolean isPlaying() ;

         void seekTo(int position) ;

         int getAudioSessionId() ;



}
