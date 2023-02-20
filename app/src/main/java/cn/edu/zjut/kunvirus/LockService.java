package cn.edu.zjut.kunvirus;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;

public class LockService extends AccessibilityService {
    private View maskViewCore;

    private void beginBindService(){
        if (maskViewCore == null){
            Context applicationContext = this.getApplicationContext();
            Object winService = this.getSystemService(WINDOW_SERVICE);
            if(winService!=null){
                maskViewCore = this.createMaskView(applicationContext,(WindowManager)winService);
            }
        }
    }

    private View createMaskView(Context context, WindowManager windowManager) {

       /* FrameLayout layout = new FrameLayout(context, (AttributeSet)null);
        layout.setLayoutParams(new ViewGroup.LayoutParams(200,200));*/
        //LayoutInflater.inflate()用于动态加载界面
        View main_view = LayoutInflater.from(context).inflate(R.layout.activity_main, (ViewGroup)null);
        main_view.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );
        /*windowManager.addView(main_view,new WindowManager.LayoutParams(
                TYPE_ACCESSIBILITY_OVERLAY, FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT));*/
        return main_view;
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
        /*AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes=AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        accessibilityServiceInfo.feedbackType=AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(accessibilityServiceInfo);*/
        beginBindService();
    }

}
