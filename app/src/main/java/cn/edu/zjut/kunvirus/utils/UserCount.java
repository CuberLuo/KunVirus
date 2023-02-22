package cn.edu.zjut.kunvirus.utils;

import android.content.Context;
import android.provider.Settings;

public class UserCount {
    static String getId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
