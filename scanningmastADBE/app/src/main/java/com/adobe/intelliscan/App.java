package com.adobe.intelliscan;

/**
 * Created by arun on 2/27/19.
 * Purpose -
 */
import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

public class App extends Application {
    private static App mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }

    public static App getContext() {
        return mContext;
    }
}
