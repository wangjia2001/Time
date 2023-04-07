package com.example.time;

import android.graphics.drawable.Drawable;

public class Week {         //  一周
    Long  usetime;           //使用总时间
    String  app;            //app名称
    Drawable icon;          //图片

    public Week(Long time, String app,Drawable icon) {
        this.usetime = time;
        this.app = app;
        this.icon=icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Long getTime() {
        return usetime;
    }

    public String getApp() {
        return app;
    }

    public void setTime(Long time) {
        this.usetime = time;
    }

    public void setApp(String app) {
        this.app = app;
    }



}
