package com.tuanchauict.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tuanchauict on 8/5/14.
 * Version 0.1.2
 */
public class Log {
    private static boolean DEBUG = true;

    private static boolean fullname;

    static class TrackingRuntime{
        private static Map<String, TrackingRuntime> map = new HashMap<>();
        private static List<TrackingRuntime> waitingList = new ArrayList<>(5);
        static TrackingRuntime factory(){
            TrackingRuntime tr;
            if(waitingList.isEmpty()){
                tr = new TrackingRuntime();
            }
            else{
                tr = waitingList.remove(0);
            }

            tr.init();
            return tr;
        }

        static void recycle(TrackingRuntime trackingRuntime){
            waitingList.add(trackingRuntime);
        }

        static void track(String tag){
            map.put(tag, factory());
        }

        static TrackingRuntime get(String tag){
            return map.get(tag);
        }

        static void remove(String tag){
            TrackingRuntime tr = map.remove(tag);
            if(tr != null){
                recycle(tr);
            }
        }

        private long t0;
        private long t1;

        void init(){
            t0 = t1 = System.currentTimeMillis();
        }

        void updateT1(){
            t1 = System.currentTimeMillis();
        }

        public long t0(){
            return t0;
        }

        public long t1(){
            return t1;
        }
    }

    public static void setFullname(boolean fullname){
    	Log.fullname = fullname;
    }

    public static void on(){
        DEBUG = true;
    }

    public static void off(){
        DEBUG = false;
    }

    public static void startTrack(String tag){
        TrackingRuntime.track(tag);
        android.util.Log.i(getStacktrace(), "[START] Tracking runtime for: [" + tag + "]");
    }

    /**
     * Print running time for tag util now.
     * @param tag
     */
    public static void printTrack(String tag){
        TrackingRuntime tr = TrackingRuntime.get(tag);
        if(tr != null){
            long dt = System.currentTimeMillis() - tr.t0();
            long dt1 = System.currentTimeMillis() - tr.t1();
            android.util.Log.i(getStacktrace(),
                    "[INFO] [" + tag + "] run in " + dt + "ms (" + time2String(dt) + "). Different from previous checking: " + dt1 + "ms (" + time2String(dt1) + ")");
            tr.updateT1();
        }
        else{
            android.util.Log.e(getStacktrace(), "[ERROR] Tracking [" + tag + "] NOT FOUND!");
        }
    }

    /**
     * Print running time for tag util now. Then remove tag out of Tracking System
     * @param tag
     */
    public static void stopTrack(String tag){
        TrackingRuntime tr = TrackingRuntime.get(tag);
        if(tr != null){
            long dt = System.currentTimeMillis() - tr.t0;
            long dt1 = System.currentTimeMillis() - tr.t1;

            android.util.Log.i(getStacktrace(), "[STOP] [" + tag + "] run in " + dt + "ms ("
                    + time2String(dt) + "). Different from previous checking: " + dt1 + "ms ("
                    + time2String(dt1) + ")");
            TrackingRuntime.remove(tag);

        }
        else{
            android.util.Log.e(getStacktrace(), "[ERROR] Tracking [" + tag + "] NOT FOUND!");
        }
    }

    private static String time2String(long time){
        long ms = time % 1000;
        String t = ms + "ms";
        time /= 1000;
        long s = time % 60;
        if(s > 0){
            t = s + "s" + t;
        }
        else{
            return t;
        }
        time /= 60;
        long m = time % 60;
        if(m > 0){
            t = m + "m" + t;
        }
        else{
            return t;
        }
        time /= 60;
        if(time > 0){
            t = time + 'h' + t;
        }

        return t;
    }


    private static String getStacktrace(){
        StackTraceElement[] es = Thread.currentThread().getStackTrace();
        StackTraceElement caller = es[4];
        String filename = caller.getFileName();
        if(!fullname){
            int index = filename.lastIndexOf("/") + 1;
            filename = filename.substring(index).replace(".java", "");
        }
        return filename + ":"+ caller.getLineNumber();
    }

    public static void here(){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), "here");
        }
    }

    public static void wtf(){
        android.util.Log.w(getStacktrace(), "WHAT THE FUCK!!!!!!!!!!!");
    }

    public static void e(String str){
        if(DEBUG){
            android.util.Log.e(getStacktrace(), str);
        }
    }

    public static void e(String str, Throwable throwable){
        if(DEBUG){
            android.util.Log.e(getStacktrace(), str, throwable);
        }
    }

    public static void e(Object[] arr){
        if(DEBUG){
            android.util.Log.e(getStacktrace(), Arrays.toString(arr));
        }
    }

    public static void e(Object[] arr, Throwable throwable){
        if(DEBUG){
            android.util.Log.e(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public static void e(List<Object> list){
        if(DEBUG){
            android.util.Log.e(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public static void e(List<Object> list, Throwable throwable){
        if(DEBUG){
            android.util.Log.e(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public static void w(String str){
        if(DEBUG){
            android.util.Log.w(getStacktrace(), str);
        }
    }

    public static void w(String str, Throwable throwable){
        if(DEBUG){
            android.util.Log.w(getStacktrace(), str, throwable);
        }
    }

    public static void w(Object[] arr){
        if(DEBUG){
            android.util.Log.w(getStacktrace(), Arrays.toString(arr));
        }
    }

    public static void w(Object[] arr, Throwable throwable){
        if(DEBUG){
            android.util.Log.w(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public static void w(List<Object> list){
        if(DEBUG){
            android.util.Log.w(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public static void w(List<Object> list, Throwable throwable){
        if(DEBUG){
            android.util.Log.w(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public static void i(String str){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), str);
        }
    }

    public static void i(String str, Throwable throwable){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), str, throwable);
        }
    }

    public static <T>void i(T[] arr){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), Arrays.toString(arr));
        }
    }

    public static <T>void i(T[] arr, Throwable throwable){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public static <T> void i(List<T> list){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public static <T> void i(List<T> list, Throwable throwable){
        if(DEBUG){
            android.util.Log.i(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public static void d(String str){
        if(DEBUG){
            android.util.Log.d(getStacktrace(), str);
        }
    }

    public static void d(String str, Throwable throwable){
        if(DEBUG){
            android.util.Log.d(getStacktrace(), str, throwable);
        }
    }

    public static void d(Object[] arr){
        if(DEBUG){
            android.util.Log.d(getStacktrace(), Arrays.toString(arr));
        }
    }

    public static void d(Object[] arr, Throwable throwable){
        if(DEBUG){
            android.util.Log.d(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public static void d(List<Object> list){
        if(DEBUG){
            android.util.Log.d(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public static void d(List<Object> list, Throwable throwable){
        if(DEBUG){
            android.util.Log.d(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public static void v(String str){
        if(DEBUG){
            android.util.Log.v(getStacktrace(), str);
        }
    }

    public static void v(String str, Throwable throwable){
        if(DEBUG){
            android.util.Log.v(getStacktrace(), str, throwable);
        }
    }

    public static void v(Object[] arr){
        if(DEBUG){
            android.util.Log.v(getStacktrace(), Arrays.toString(arr));
        }
    }

    public static void v(Object[] arr, Throwable throwable){
        if(DEBUG){
            android.util.Log.v(getStacktrace(), Arrays.toString(arr));
        }
    }

    public static void v(List<Object> list){
        if(DEBUG){
            android.util.Log.v(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public static void v(List<Object> list, Throwable throwable){
        if(DEBUG){
            android.util.Log.v(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }
}
