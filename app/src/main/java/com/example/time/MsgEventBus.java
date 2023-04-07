package com.example.time;

import org.greenrobot.eventbus.EventBus;

public class MsgEventBus {
    private static EventBus mEventBus;

    public static EventBus getInstance()
    {
        if (mEventBus == null)
        {
            mEventBus = new EventBus();
        }
        return mEventBus;
    }
}
