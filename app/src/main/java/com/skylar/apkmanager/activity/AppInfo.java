package com.skylar.apkmanager.activity;

import android.graphics.drawable.Drawable;
import java.io.Serializable;

/**
 * Created by Skylar on 2017/9/25.
 */

public class AppInfo implements Serializable {

    private String name;
    private Drawable icon;
    private String version;
    private String packageName;
    private String path;
    private String size;
    private boolean isSystem;

    public AppInfo(String name,Drawable icon,String version,String packageName,String path,String size,boolean isSystem){
        this.name = name;
        this.icon = icon;
        this.version = version;
        this.packageName = packageName;
        this.path = path;
        this.size = size;
        this.isSystem = isSystem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

}
