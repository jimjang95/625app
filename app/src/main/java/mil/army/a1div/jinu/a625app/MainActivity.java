package mil.army.a1div.jinu.a625app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Scanner;

public class MainActivity extends Activity {
    private MusicService mService;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.ServiceBinder binder = (MusicService.ServiceBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"sadanFont.ttf");

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final TextView textView = (TextView) findViewById(R.id.fallenText);
        TextView textView1 = (TextView) findViewById(R.id.title1);
        TextView textView2 = (TextView) findViewById(R.id.title2);
        textView.setTypeface(typeface);
        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(getAssets().open("junsaja625.csv"), "UTF-8"));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                textView.append(line);
                textView.append("\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(int i=0; i<100; i++) {
//            textView.append("아아아아아아아아아아아아아아\n\n");
//        }

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator autoScroll = ObjectAnimator.ofInt(scrollView, "scrollY", textView.getHeight()).setDuration(500000);
                autoScroll.start();

                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //Log.d("textView", String.valueOf(textView.getWidth()) + ", " + String.valueOf(textView.getHeight()));
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                autoScroll.pause();
                                Log.d("MAIN ACTIVITY/ON_TOUCH", "action_DOWN");
                            }
                            case MotionEvent.ACTION_MOVE : {
                                Log.d("MAIN ACTIVITY/ON_TOUCH", "action_MOVE");
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                Log.d("MAIN ACTIVITY/ON_TOUCH", "action_UP");
                                autoScroll.start();
                                break;
                            }
                        }
                        return true;
                    }
                });
            }
        });

        /*File file = new File();   //파일 경로
        try {
            FileReader fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        final ImageButton playStop = findViewById(R.id.button2);
        playStop.setBackgroundResource(R.drawable.start);
        playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBound) {
                    return;
                }
                if (!mService.mPlayer.isPlaying()) {
                    mService.startMusic();
                    playStop.setBackgroundResource(R.drawable.stop);
                } else {
                    mService.stopMusic();
                    playStop.setBackgroundResource(R.drawable.start);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
