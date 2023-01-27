package cn.edu.zjut.myapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    static MediaPlayer player;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(getApplicationContext(), R.raw.cxk_music);
        player.setLooping(true);
        try {
            //因为MediaPlayer的create已经调用了prepare方法，因此此处直接start方法即可
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {	//停止服务
        super.onDestroy();
        if(player!=null){
            player.stop();
            player.release();
            player = null;
        }
    }
}
