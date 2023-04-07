package com.example.time;

import android.graphics.drawable.Drawable;

public class App {
    private String name;//软件名称
    private Drawable image;
    private String time; //使用时间

    public App(String name, Drawable img, String time) {
        this.name = name;
        this.image = img;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Drawable img) {
        this.image = img;
    }

    public Drawable getImage() {
        return image;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
