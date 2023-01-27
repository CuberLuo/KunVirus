package cn.edu.zjut.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

public class VoiceVolumeWrapper {

    private static final String TAG = "Main_VoiceVolumeWrapper";

    private AudioManager _AudioManager;
    private Context _Context;

    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";

    private MyVolumeReceiver mVolumeReceiver;

    public VoiceVolumeWrapper(Context context){

        _Context = context;
        _AudioManager =(AudioManager) _Context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int GetMusicVoiceMax(){

        return  _AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ;
    }

    public int GetMusicVoiceMin(){

        return  _AudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC) ;
    }

    public int GetMusicVoiceCurrentValue(){

        return  _AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) ;
    }
    //增加音乐音量
    public void AddMusicVoiceVolume(int value){
        int addValue = (GetMusicVoiceCurrentValue() + value) ;
        // 防止音量值越界
        addValue = Math.min(addValue, GetMusicVoiceMax());
        _AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,addValue,AudioManager.FLAG_PLAY_SOUND);
    }
    //减少音乐音量
    public void ReduceMusicVoiceVolume(int value){
        int reduceValue = (GetMusicVoiceCurrentValue() - value) ;
        // 防止音量值越界
        reduceValue = Math.max(reduceValue, GetMusicVoiceMin());
        _AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,reduceValue,AudioManager.FLAG_PLAY_SOUND);
    }

    //注册广播监听
    public void registerVolumeReceiver() {
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        _Context.registerReceiver(mVolumeReceiver, filter);
    }

    //取消注册广播监听
    public void unregisterVolumeReceiver() {

        if (mVolumeReceiver != null) _Context.unregisterReceiver(mVolumeReceiver);

    }

    //音量变化广播类
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isReceiveVolumeChange(intent)) {
                int currVolume = _AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //Toast.makeText(context, currVolume + " ", Toast.LENGTH_SHORT).show();
                AddMusicVoiceVolume(30);//检测到用户音量变化直接增加音量
            }
        }
    }

    //判断是否是音乐音量变化（音量键改变的音量）
    private boolean isReceiveVolumeChange(Intent intent) {
        return intent.getAction() != null
                && intent.getAction().equals(ACTION_VOLUME_CHANGED)
                && intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC
                ;
    }

}