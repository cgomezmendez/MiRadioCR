package me.cristiangomez.miradiocr.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends ActionBarActivity {
    private static final String ADS_URL = "http://clicksmiradio.tk/publicidad.php";
    private PlayerService.PlayerBinder playerBinder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentPlayer = new Intent(this,PlayerService.class);
        bindService(intentPlayer,serviceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void play(View view){
        try {
            playerBinder.play();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void pause(View view){
        try {
            playerBinder.pause();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void off(View view){
            Intent intentPlayer = new Intent(this,PlayerService.class);
            stopService(intentPlayer);
            this.finish();

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            Log.d(MainActivity.class.getName(), "Servicio conectado");
            playerBinder = (PlayerService.PlayerBinder) binder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            playerBinder = null;
        }
    };


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            WebView webview = (WebView) rootView.findViewById(R.id.webviewAds);
            webview.setInitialScale(1);
            webview.setHorizontalScrollBarEnabled(false);
            webview.loadUrl(ADS_URL);
            WebView songView = (WebView) rootView.findViewById(R.id.songWebView);
            songView.setBackgroundColor(0x00000000);
            songView.setClickable(false);
            songView.loadUrl("http://clicksmiradio.tk/estado.php");
            return rootView;
        }

    }

}
