package cn.edu.zjut.kunvirus.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.zjut.kunvirus.R;

public class MainActivity extends AppCompatActivity {
    private static int screen_vary_cnt=0;
    private final int vary_delay=500;

    /*class BrightnessTimerTask extends TimerTask {
        public void run() {
            screen_vary_cnt=(screen_vary_cnt+1)%2;//0101...这样变化
            if(screen_vary_cnt==1)
                MainActivity.this.runOnUiThread(() -> setWindowBrightness(0));
            else
                MainActivity.this.runOnUiThread(() -> setWindowBrightness(255));
            new Timer().schedule(new BrightnessTimerTask(), vary_delay);
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new Timer().schedule(new BrightnessTimerTask(),vary_delay);
        /*AmazingUtil.openNotifyService(this);
        */

        //Toast.makeText(this,"唱跳rap篮球",Toast.LENGTH_LONG).show();
        //Toast.makeText(this,UserCount.getId(this),Toast.LENGTH_LONG).show();
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

    /*private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }*/
}