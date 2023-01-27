package cn.edu.zjut.myapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        returnHome();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMaxVolume();//音量调节到最大
        VoiceVolumeWrapper voiceVolumeWrapper = new VoiceVolumeWrapper(this);
        voiceVolumeWrapper.registerVolumeReceiver();//监听音量变化
        Intent service = new Intent(this,MusicService.class);
        this.startService(service);	//开启服务保证音乐在后台运行
        Toast.makeText(this,"唱跳rap篮球",Toast.LENGTH_SHORT).show();
    }

    public void setMaxVolume(){
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume =mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,0);
    }

    public void returnHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//提示如果是服务里调用，必须加入new task标识
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


}