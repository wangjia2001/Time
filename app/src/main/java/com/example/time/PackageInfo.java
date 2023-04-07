package com.example.time;

public class PackageInfo {
    private int      mUsedCount;
    private long     mUsedTime;
    private String   mPackageName;

    public PackageInfo(int mUsedCount, long mUsedTime, String mPackageName) {
        this.mUsedCount = mUsedCount;
        this.mUsedTime = mUsedTime;
        this.mPackageName = mPackageName;
    }

    public void addCount(){
        mUsedCount++;
    }

    public int getmUsedCount() {
        return mUsedCount;
    }

    public void setmUsedCount(int mUsedCount) {
        this.mUsedCount = mUsedCount;
    }

    public long getmUsedTime() {
        return mUsedTime;
    }

    public void setmUsedTime(long mUsedTime) {
        this.mUsedTime = mUsedTime;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    @Override
    public boolean equals(Object o) {
        //return super.equals(o);
        if(o == null) return false;
        if(this == o) return true;
        PackageInfo standardDetail = ( PackageInfo )o;
        if( standardDetail.getmPackageName().equals(this.mPackageName) ){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        //return super.hashCode();
        return (mPackageName + mUsedTime).hashCode();
    }
}