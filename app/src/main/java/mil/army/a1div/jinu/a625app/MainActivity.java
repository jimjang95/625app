package mil.army.a1div.jinu.a625app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends Activity {
    private BufferedReader bufferedReader = null;
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

        Date today = new Date();
        int month = today.getMonth() + 1;
        int day = today.getDate();
        String fileName = "sortByDate/";
        if (month < 10) {
            fileName += '0';
        }
        fileName += month;
        if (day < 10) {
            fileName += '0';
        }
        fileName += day;
        fileName += ".csv";

        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("ControlTest", fileName);
        FileController controller = new FileController(bufferedReader);
        for (Soldier s : controller.getSoldiers()) {
            Log.d("ControlTest", s.toString());
        }

        Soldier[] soldiers = controller.getSoldiers().toArray(new Soldier[controller.size]);
        Log.d("전사자 명부 총원", String.valueOf(soldiers.length) + "명");

        Arrays.sort(soldiers, new Comparator<Soldier>() {
            @Override
            public int compare(Soldier a, Soldier b) {
                if (a.getYear() == b.getYear()) {
                    if (a.getSosok() == b.getSosok()) {
                        return b.getRank().ordinal() - a.getRank().ordinal();
                    } else {
                        return a.getSosok().ordinal() - b.getSosok().ordinal();
                    }
                } else {
                    return a.getYear() - b.getYear();
                }
            }
        });

        //final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final TextView textView = (TextView) findViewById(R.id.fallenText);
        TextView textView1 = (TextView) findViewById(R.id.title1);
        TextView textView2 = (TextView) findViewById(R.id.title2);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"sadanFont.ttf");
        //textView.setTypeface(typeface);
        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);

        for(int i=0; i<soldiers.length; i++) {
            textView.append(String.valueOf(soldiers[i].getYear()) + ". " +
            String.valueOf(soldiers[i].getMonth()) + ". " + String.valueOf(soldiers[i].getDay()) + "\t" +
            soldiers[i].getSosok() + "\t" + soldiers[i].nameRank() + "\n");
        }

        /*scrollView.post(new Runnable() {
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
        });*/

        final ImageButton playStop = findViewById(R.id.playStop);
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

        mService.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playStop.setBackgroundResource(R.drawable.start);
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
