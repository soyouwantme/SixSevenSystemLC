package com.kk.sixsevensystemlc;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by BinaryHB on 16/9/13.
 */
public class GetStartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"Ox3NbV9pV7O5qOyC71SNDnCI-9Nh9j0Va", "Q5BCakoKXl8iUScthf1nqq0T");
        /**在应用开发阶段，你可以选择开启 SDK 的调试日志（debug log）来方便追踪问题。调试日志开启后，
         * SDK 会把网络请求、错误消息等信息输出到 IDE 的日志窗口，或是浏览器 Console 或是 LeanClo
         * ud 控制台的 云引擎日志 中。*/
        AVOSCloud.setDebugLogEnabled(true);
    }
}