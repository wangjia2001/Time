package com.example.time;

public class TimeEvent {
    private long mStartTime;
    private long mEndTime;

    public TimeEvent(long mStartTime, long mEndTime) {
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }
}
