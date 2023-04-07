package com.example.time;


import java.text.DecimalFormat;


public class PieData {
    private String text;
    private float value;
    private float Angle;
    private float Percentage;
    private int color;
    private float CurrentStartAngle;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getCurrentStartAngle() {
        return CurrentStartAngle;
    }

    public void setCurrentStartAngle(float currentStartAngle) {
        CurrentStartAngle = currentStartAngle;
    }

    public String getPercentage() {
        DecimalFormat decimalFormat = new DecimalFormat("00.0");
        return decimalFormat.format(Percentage*100) + "%";
    }


    public void setPercentage(float percentage) {
        //保留两位小数点
        Percentage = percentage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getAngle() {
        return Angle;
    }

    public void setAngle(float angle) {
        Angle = angle;
    }

    public PieData() {
    }

    public PieData(float value) {
        this.value = value;
    }

    public PieData(String text, float value) {
        this.text = text;
        this.value = value;
    }
}