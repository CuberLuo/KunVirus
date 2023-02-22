package cn.edu.zjut.kunvirus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

import cn.edu.zjut.kunvirus.R;
import cn.edu.zjut.kunvirus.utils.NetUtils;

public class TipsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStartSignal();
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
        setContentView(R.layout.activity_tips);

    }

    private void setStartSignal(){
        String content = "cxk000";
        try {
            FileOutputStream os = openFileOutput("cxk.txt", MODE_PRIVATE);
            //写数据
            os.write(content.getBytes());
            os.close();//关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean serviceOpenCheck(){
        try {
            int accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            //Log.i("CuberLuo", String.valueOf(accessibilityEnabled));
            if (accessibilityEnabled == 1){
                String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                //Log.i("CuberLuo",settingValue);
                if(settingValue.contains("cn.edu.zjut.kunvirus.service.LockService")){
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
}
