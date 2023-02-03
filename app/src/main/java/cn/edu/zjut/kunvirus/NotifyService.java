package cn.edu.zjut.kunvirus;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                setMyNotify();
            }
        },0,2,TimeUnit.SECONDS);

    }

    public void setMyNotify(){
        initNotify("紧急通知", "你干嘛~哈哈~哎呦~");
    }

    public void initNotify(String title, String context) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent nfIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfIntent, PendingIntent.FLAG_IMMUTABLE);
        //String channel_id = String.valueOf((int)(Math.random() * 10000));//通知id
        String channel_id = "100";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setContentIntent(pendingIntent) // 设置PendingIntent
                .setSmallIcon(R.mipmap.ic_launcher_round) // 设置状态栏内的小图标
                .setContentTitle(title)
                .setContentText(context) // 设置内容
                .setVisibility(VISIBILITY_PUBLIC)// 锁屏显示全部通知
                .setCategory(Notification.CATEGORY_SERVICE)//设置类别
                .setPriority(PRIORITY_MAX);// 优先级

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //安卓8.0以上系统要求通知设置Channel,否则会报错
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "服务常驻通知", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);//锁屏显示全部通知
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channel_id);
        }
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.flags = Notification.FLAG_NO_CLEAR;//不消失的常驻通知
        startForeground(1, notification);//设置常驻通知
    }
}
