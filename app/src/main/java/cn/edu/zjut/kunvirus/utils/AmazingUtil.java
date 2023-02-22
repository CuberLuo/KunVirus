package cn.edu.zjut.kunvirus.utils;

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

import cn.edu.zjut.kunvirus.R;
import cn.edu.zjut.kunvirus.service.MusicService;
import cn.edu.zjut.kunvirus.service.NotifyService;

public class AmazingUtil {
    public static void SetWallPaper(Context context) {
        try {
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.cxk_bg);
            WallpaperManager manager = WallpaperManager.getInstance(context);
            manager.setBitmap(bitmap.getBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SetLockWallPaper(Context context) {
        try {
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(context,R.drawable.cxk_lock_bg);
            WallpaperManager manager = WallpaperManager.getInstance(context);
            manager.setBitmap(bitmap.getBitmap(),null,true,WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void shakeItBaby(Context context) {
        //5分钟振动
        int vibrate_time=5*60*1000;
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(vibrate_time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(vibrate_time);
        }
    }

    public static void setMaxVolume(Context context){
        AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume =mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,AudioManager.FLAG_PLAY_SOUND);
    }

    public static void openNotifyService(Context context){
        Intent notifyService = new Intent(context, NotifyService.class);
        context.startService(notifyService);	//开启通知服务
    }

    public static void openVolumeListener(Context context){
        VoiceVolumeWrapper voiceVolumeWrapper = new VoiceVolumeWrapper(context);
        voiceVolumeWrapper.registerVolumeReceiver();//监听音量变化
    }

    public static void openMusicService(Context context){
        Intent musicService = new Intent(context, MusicService.class);
        context.startService(musicService);	//开启服务保证音乐在后台运行
    }

    /*static void changeIconAndName(Context context){
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context,context.getPackageName()+
                ".NewMainActivity"),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context,context.getPackageName()+
                ".MainActivity"),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
    }

    static void rebootApp(Context context){
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }*/
}
