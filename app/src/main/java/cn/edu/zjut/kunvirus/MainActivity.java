package cn.edu.zjut.kunvirus;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //returnHome();
        shakeItBaby();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setText();
        setImage();
        setMaxVolume();//音量调节到最大
        cn.edu.zjut.kunvirus.VoiceVolumeWrapper voiceVolumeWrapper = new cn.edu.zjut.kunvirus.VoiceVolumeWrapper(this);
        voiceVolumeWrapper.registerVolumeReceiver();//监听音量变化
        Intent musicService = new Intent(this, cn.edu.zjut.kunvirus.MusicService.class);
        this.startService(musicService);	//开启服务保证音乐在后台运行
        Intent notifyService = new Intent(this, cn.edu.zjut.kunvirus.NotifyService.class);
        this.startService(notifyService);	//开启通知服务
        Toast.makeText(this,"唱跳rap篮球",Toast.LENGTH_SHORT).show();

    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        *//**
         * event.getRepeatCount() 重复次数,点后退键的时候，为了防止点得过快，触发两次后退事件，故做此设置。
         *//*
        if ((
                keyCode == KeyEvent.KEYCODE_BACK
                //|| keyCode == KeyEvent.KEYCODE_HOME
                //|| keyCode == KeyEvent.KEYCODE_MENU
        )
                && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }*/


    public void setMaxVolume(){
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume =mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,0);
    }

    public void returnHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void setImage(){
        ImageView img_view = findViewById(R.id.imageView);
        img_view.setImageResource(R.drawable.ikun);
    }
    public void setText(){
        TextView text_view=findViewById(R.id.textView);
        text_view.setText("ikun");
        text_view.setTextSize(30);
        text_view.setGravity(Gravity.CENTER);
    }
    //手机振动
    private void shakeItBaby() {
        //5分钟振动
        int vibrate_time=5*60*1000;
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(vibrate_time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(vibrate_time);
        }
    }


}