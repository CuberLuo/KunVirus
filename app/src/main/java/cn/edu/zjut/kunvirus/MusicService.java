package cn.edu.zjut.kunvirus;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    static MediaPlayer ganma_player;
    static MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ganma_player=MediaPlayer.create(getApplicationContext(), R.raw.ni_gan_ma);
        player = MediaPlayer.create(getApplicationContext(), R.raw.cxk_music);
        player.setLooping(true);
        ganma_player.start();
        ganma_player.setOnCompletionListener(mp -> {
            player.start();
        });

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
