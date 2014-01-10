package me.cristiangomez.miradiocr.app;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Cristian on 01/09/14.
 */
public class PlayerPrepareListener implements MediaPlayer.OnPreparedListener{
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        Log.d(PlayerPrepareListener.class.getName(),String.valueOf(mediaPlayer.isPlaying()));
    }
}
