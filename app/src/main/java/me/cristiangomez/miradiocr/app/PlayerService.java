package me.cristiangomez.miradiocr.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.MediaController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Cristian on 01/09/14.
 */
public class PlayerService extends Service{
    private static MediaPlayer mediaPlayer = null;
    private static final String SOURCE_URL = "http://www.miradiocr.com:9998";
    private final IBinder binder = new PlayerBinder();
    private NotificationManager notificationManager = null;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            getMediaPlayer().setOnPreparedListener(new PlayerPrepareListener());
            getMediaPlayer().prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher,"MiRadioCR",System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        notification.setLatestEventInfo(this, "MiRadioCr", "",
                PendingIntent.getActivity(this, 1, intent, 0));
        notificationManager.notify(1,notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getMediaPlayer().stop();
            getMediaPlayer().release();
            mediaPlayer = null;
            notificationManager.cancelAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(PlayerService.class.getName(), "Testing");
        return binder;
    }
    private MediaPlayer getMediaPlayer() throws IOException {
        if (mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(SOURCE_URL);
        }
        return mediaPlayer;
    }


    public class PlayerBinder extends Binder{
        PlayerService getPlayerService(){
            return PlayerService.this;
        }
        void play() throws IOException {
            if (!PlayerService.this.getMediaPlayer().isPlaying()){
                PlayerService.this.getMediaPlayer().start();
            }
        }
        void pause() throws IOException {
            if (PlayerService.this.getMediaPlayer().isPlaying()){
                PlayerService.this.getMediaPlayer().pause();
            }
        }
        void stop() throws IOException {
            if (PlayerService.this.getMediaPlayer().isPlaying()) {
                PlayerService.this.getMediaPlayer().stop();
                PlayerService.this.getMediaPlayer().release();
                PlayerService.mediaPlayer = null;
            }
        }
    }
}
