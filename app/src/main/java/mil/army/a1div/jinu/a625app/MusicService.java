package mil.army.a1div.jinu.a625app;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;


    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MUSIC SERVICE", "I actually bind???");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicService", "MusicService created");

        mPlayer = MediaPlayer.create(this, R.raw.muknyum);
        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
        }

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                onError(mPlayer, i, i1);
                return true;
            }
        });
        Log.d("MUSIC SERVICE", "onCreate() end!! is it null? " + (mPlayer == null));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void startMusic() {
        try {
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    Log.d("MUSIC_SERVICE", "onPrepared()");
                    mp.seekTo(0);
                    mp.start();
                }
            });
        } catch (IllegalStateException e) {
            Log.d("MUSIC_SERVICE", "caught IllegalState!");
            mPlayer.seekTo(0);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MUSIC SERVICE", "OnDestroy() Called!! + is it null?" +  (mPlayer == null));
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if(mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
