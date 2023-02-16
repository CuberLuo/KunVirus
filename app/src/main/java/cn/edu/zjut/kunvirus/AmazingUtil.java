package cn.edu.zjut.kunvirus;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.content.ContextCompat;

import java.io.IOException;

public class AmazingUtil {
    static void SetWallPaper(Context context) {
        try {
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(context,R.drawable.cxk_bg);
            WallpaperManager manager = WallpaperManager.getInstance(context);
            manager.setBitmap(bitmap.getBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void SetLockWallPaper(Context context) {
        try {
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(context,R.drawable.cxk_lock_bg);
            WallpaperManager manager = WallpaperManager.getInstance(context);
            manager.setBitmap(bitmap.getBitmap(),null,true,WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static void shakeItBaby(Context context) {
        //5分钟振动
        int vibrate_time=5*60*1000;
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(vibrate_time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(vibrate_time);
        }
    }

    static void setMaxVolume(Context context){
        AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume =mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,AudioManager.FLAG_PLAY_SOUND);
    }

    static void openNotifyService(Context context){
        Intent notifyService = new Intent(context, NotifyService.class);
        context.startService(notifyService);	//开启通知服务
    }

    static void openVolumeListener(Context context){
        VoiceVolumeWrapper voiceVolumeWrapper = new VoiceVolumeWrapper(context);
        voiceVolumeWrapper.registerVolumeReceiver();//监听音量变化
    }

    static void openMusicService(Context context){
        Intent musicService = new Intent(context, MusicService.class);
        context.startService(musicService);	//开启服务保证音乐在后台运行
    }
}
