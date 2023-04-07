package com.example.time;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class EventUtils {
    public  static final String           TAG         = "EventUtils";
    private static final SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<UsageEvents.Event> getEventList(Context context, long startTime, long endTime){
        ArrayList<UsageEvents.Event> mEventList = new ArrayList<>();
        Long t=new Long(startTime);//time是从服务器拿到的时间戳
        t=t+8*60*60*1000;           //因为时间会少8个小时所以要做变换
        Long t1=new Long(endTime);//time是从服务器拿到的时间戳
        t1=t1+8*60*60*1000;           //因为时间会少8个小时所以要做变换
        Date date = new Date(t);
        Date date1 = new Date(t1);
//        Log.i(TAG," EventUtils-getEventList()   Range start:" + startTime);
//        Log.i(TAG," EventUtils-getEventList()   Range end:" +endTime);
//        Log.i(TAG," EventUtils-getEventList()   Range start:" + dateFormat.format(date));
//        Log.i(TAG," EventUtils-getEventList()   Range end:" + dateFormat.format(date1));

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        UsageEvents events = mUsmManager.queryEvents(startTime, endTime);
        while (events.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            events.getNextEvent(e);
            if (e != null && (e.getEventType() == 1 || e.getEventType() == 2)) {
//                Log.i(TAG," EventUtils-getEventList()  "+e.getTimeStamp()+"   event:" + e.getClassName() + "   type = " + e.getEventType());
                mEventList.add(e);
//                Log.i(TAG," EventUtils-getEventList()  "+e.getTimeStamp()+"   event:" + e.getClassName() + "   type = " + e.getEventType());
            }
        }

        return mEventList;
    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<UsageStats> getUsageList(Context context, long startTime, long endTime) {
        Long t=new Long(startTime);//time是从服务器拿到的时间戳
        t=t+8*60*60*1000;           //因为时间会少8个小时所以要做变换
        Long t1=new Long(endTime);//time是从服务器拿到的时间戳
        t1=t1+8*60*60*1000;           //因为时间会少8个小时所以要做变换
        Date date = new Date(t);
        Date date1 = new Date(t1);
//        Log.i(TAG," EventUtils-getUsageList()   Range start:" + startTime);
//        Log.i(TAG," EventUtils-getUsageList()   Range end:" + endTime);
//        Log.i(TAG," EventUtils-getUsageList()   Range start:" + dateFormat.format(date));
//        Log.i(TAG," EventUtils-getUsageList()   Range end:" + dateFormat.format(date1));

        ArrayList<UsageStats> list = new ArrayList<>();

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        Map<String, UsageStats> map = mUsmManager.queryAndAggregateUsageStats(startTime, endTime);
        for (Map.Entry<String, UsageStats> entry : map.entrySet()) {
            UsageStats stats = entry.getValue();
            if(stats.getTotalTimeInForeground() > 0){
                list.add(stats);
//                Log.i(TAG," EventUtils-getUsageList()   stats:" + stats.getPackageName() + "   TotalTimeInForeground = " + stats.getTotalTimeInForeground());
            }
        }

        return list;
    }
}
