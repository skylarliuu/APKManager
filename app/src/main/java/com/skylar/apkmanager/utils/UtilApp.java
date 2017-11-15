package com.skylar.apkmanager.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Skylar on 2017/9/25.
 */

public class UtilApp {

    public static final int Permission_RequestCode = 1;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity act){
        boolean result = false;
        if(ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},Permission_RequestCode);
        }else{
            result = true;
        }
        return result;
    }
}
