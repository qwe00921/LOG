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
        private static Map<String, TrackingRuntime> map = new HashMap<String, TrackingRuntime>();
        private static List<TrackingRuntime> waitingList = new ArrayList<TrackingRuntime>(5);
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
        StackTraceElement caller = es[6];
        String filename = caller.getFileName();
        if(!fullname){
            int index = filename.lastIndexOf("/") + 1;
            filename = filename.substring(index).replace(".java", "");
        }
        return filename + ":"+ caller.getLineNumber();
    }

    private static String tagToString(String tag){
        if(tag != null){
            tag = tag + ":" + getStacktrace();
        }
        else{
            tag = getStacktrace();
        }

        return tag;
    }

    public static void here(){
        if(DEBUG){
            info(null, "here", null);
        }
    }

    public static void wtf(){
        warning(null, "WHAT THE FUCK!!!!!!!!!!!", null);
    }

    private static <T>void error(String tag, T mess, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            android.util.Log.e(tag, String.valueOf(mess), throwable);
        }
    }

    private static <T> void error(String tag, T[] array, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(array == null){
                    android.util.Log.e(tag, "Array is NULL", throwable);
                }
                else{
                    android.util.Log.e(tag, "Array's length = " + array.length);
                    for(int i = 0; i < array.length; i++){
                        android.util.Log.e(tag, String.format("[%3d] %s", i, String.valueOf(array[i])));
                    }
                }
            }
            else{
                android.util.Log.e(tag, Arrays.toString(array), throwable);
            }
        }
    }

    private static <T> void error(String tag, List<T> list, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(list == null){
                    android.util.Log.e(tag, "List is NULL", throwable);
                }
                else{
                    android.util.Log.e(tag, "List's size = " + list.size());
                    int l = list.size();
                    for(int i = 0; i < l; i++){
                        android.util.Log.e(tag, String.format("[%3d] %s", i, String.valueOf(list.get(i))));
                    }
                }
            }
            else{
                android.util.Log.e(tag, Arrays.toString(list.toArray()), throwable);
            }
        }
    }

    public static<T> void e(String message){
        error(null, message, null);
    }

    public static<T> void e(String tag, String message){
        error(tag, message, null);
    }

    public static<T> void e(String message, Throwable throwable){
        error(null, message, throwable);
    }

    public static<T> void e(String tag, String message, Throwable throwable){
        error(tag, message, throwable);
    }

    public static<T> void e(T[] array){
        error(null, array, true, null);
    }

    public static<T> void e(String tag, T[] array){
        error(tag, array, true, null);
    }

    public static<T> void e(T[] array, Throwable throwable){
        error(null, array, true, throwable);
    }

    public static<T> void e(String tag, T[] array, Throwable throwable){
        error(tag, array, true, throwable);
    }

    public static<T> void e(T[] array, boolean lines, Throwable throwable){
        error(null, array, lines, throwable);
    }

    public static<T> void e(String tag, T[] array, boolean lines, Throwable throwable){
        error(tag, array, lines, throwable);
    }

    public static<T> void e(List<T> list){
        error(null, list, true, null);
    }

    public static<T> void e(List<T> list, boolean lines){
        error(null, list, lines, null);
    }

    public static<T> void e(String tag, List<T> list){
        error(tag, list, true, null);
    }

    public static<T> void e(String tag, List<T> list, boolean lines){
        error(tag, list, lines, null);
    }

    public static<T> void e(List<T> list, Throwable throwable){
        error(null, list, true, throwable);
    }

    public static<T> void e(String tag, List<T> list, Throwable throwable){
        error(tag, list, true, throwable);
    }

    public static<T> void e(List<T> list, boolean lines, Throwable throwable){
        error(null, list, lines, throwable);
    }

    public static<T> void e(String tag, List<T> list, boolean lines, Throwable throwable){
        error(tag, list, lines, throwable);
    }


    private static <T>void warning(String tag, T mess, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            android.util.Log.w(tag, String.valueOf(mess), throwable);
        }
    }

    private static <T> void warning(String tag, T[] array, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(array == null){
                    android.util.Log.w(tag, "Array is NULL", throwable);
                }
                else{
                    android.util.Log.w(tag, "Array's length = " + array.length);
                    for(int i = 0; i < array.length; i++){
                        android.util.Log.w(tag, String.format("[%3d] %s", i, String.valueOf(array[i])));
                    }
                }
            }
            else{
                android.util.Log.w(tag, Arrays.toString(array), throwable);
            }
        }
    }

    private static <T> void warning(String tag, List<T> list, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(list == null){
                    android.util.Log.w(tag, "List is NULL", throwable);
                }
                else{
                    android.util.Log.w(tag, "List's size = " + list.size());
                    int l = list.size();
                    for(int i = 0; i < l; i++){
                        android.util.Log.w(tag, String.format("[%3d] %s", i, String.valueOf(list.get(i))));
                    }
                }
            }
            else{
                android.util.Log.w(tag, Arrays.toString(list.toArray()), throwable);
            }
        }
    }

    public static<T> void w(String message){
        warning(null, message, null);
    }

    public static<T> void w(String tag, String message){
        warning(tag, message, null);
    }

    public static<T> void w(String message, Throwable throwable){
        warning(null, message, throwable);
    }

    public static<T> void w(String tag, String message, Throwable throwable){
        warning(tag, message, throwable);
    }

    public static<T> void w(T[] array){
        warning(null, array, true, null);
    }

    public static<T> void w(T[] array, boolean lines){
        warning(null, array, lines, null);
    }

    public static<T> void w(String tag, T[] array){
        warning(tag, array, true, null);
    }

    public static<T> void w(String tag, T[] array, boolean lines){
        warning(tag, array, lines, null);
    }

    public static<T> void w(T[] array, Throwable throwable){
        warning(null, array, true, throwable);
    }

    public static<T> void w(String tag, T[] array, Throwable throwable){
        warning(tag, array, true, throwable);
    }

    public static<T> void w(T[] array, boolean lines, Throwable throwable){
        warning(null, array, lines, throwable);
    }

    public static<T> void w(String tag, T[] array, boolean lines, Throwable throwable){
        warning(tag, array, lines, throwable);
    }

    public static<T> void w(List<T> list){
        warning(null, list, true, null);
    }

    public static<T> void w(List<T> list, boolean lines){
        warning(null, list, lines, null);
    }

    public static<T> void w(String tag, List<T> list){
        warning(tag, list, true, null);
    }

    public static<T> void w(String tag, List<T> list, boolean lines){
        warning(tag, list, lines, null);
    }

    public static<T> void w(List<T> list, Throwable throwable){
        warning(null, list, true, throwable);
    }

    public static<T> void w(String tag, List<T> list, Throwable throwable){
        warning(tag, list, true, throwable);
    }

    public static<T> void w(List<T> list, boolean lines, Throwable throwable){
        warning(null, list, lines, throwable);
    }

    public static<T> void w(String tag, List<T> list, boolean lines, Throwable throwable){
        warning(tag, list, lines, throwable);
    }


    private static <T>void info(String tag, T mess, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            android.util.Log.i(tag, String.valueOf(mess), throwable);
        }
    }

    private static <T> void info(String tag, T[] array, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(array == null){
                    android.util.Log.i(tag, "Array is NULL", throwable);
                }
                else{
                    android.util.Log.i(tag, "Array's length = " + array.length);
                    for(int i = 0; i < array.length; i++){
                        android.util.Log.i(tag, String.format("[%3d] %s", i, String.valueOf(array[i])));
                    }
                }
            }
            else{
                android.util.Log.i(tag, Arrays.toString(array), throwable);
            }
        }
    }

    private static <T> void info(String tag, List<T> list, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(list == null){
                    android.util.Log.i(tag, "List is NULL", throwable);
                }
                else{
                    android.util.Log.i(tag, "List's size = " + list.size());
                    int l = list.size();
                    for(int i = 0; i < l; i++){
                        android.util.Log.i(tag, String.format("[%3d] %s", i, String.valueOf(list.get(i))));
                    }
                }
            }
            else{
                android.util.Log.i(tag, Arrays.toString(list.toArray()), throwable);
            }
        }
    }

    public static<T> void i(String message){
        info(null, message, null);
    }

    public static<T> void i(String tag, String message){
        info(tag, message, null);
    }

    public static<T> void i(String message, Throwable throwable){
        info(null, message, throwable);
    }

    public static<T> void i(String tag, String message, Throwable throwable){
        info(tag, message, throwable);
    }

    public static<T> void i(T[] array){
        info(null, array, true, null);
    }

    public static<T> void i(T[] array, boolean lines){
        info(null, array, lines, null);
    }

    public static<T> void i(String tag, T[] array, boolean lines){
        info(tag, array, lines, null);
    }

    public static<T> void i(T[] array, Throwable throwable){
        info(null, array, true, throwable);
    }

    public static<T> void i(String tag, T[] array, Throwable throwable){
        info(tag, array, true, throwable);
    }

    public static<T> void i(T[] array, boolean lines, Throwable throwable){
        info(null, array, lines, throwable);
    }

    public static<T> void i(String tag, T[] array, boolean lines, Throwable throwable){
        info(tag, array, lines, throwable);
    }

    public static<T> void i(List<T> list){
        info(null, list, true, null);
    }

    public static<T> void i(List<T> list, boolean lines){
        info(null, list, lines, null);
    }

    public static<T> void i(String tag, List<T> list){
        info(tag, list, true, null);
    }

    public static<T> void i(String tag, List<T> list, boolean lines){
        info(tag, list, lines, null);
    }

    public static<T> void i(List<T> list, Throwable throwable){
        info(null, list, true, throwable);
    }

    public static<T> void i(String tag, List<T> list, Throwable throwable){
        info(tag, list, true, throwable);
    }

    public static<T> void i(List<T> list, boolean lines, Throwable throwable){
        info(null, list, lines, throwable);
    }

    public static<T> void i(String tag, List<T> list, boolean lines, Throwable throwable){
        info(tag, list, lines, throwable);
    }


    private static <T>void debug(String tag, T mess, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            android.util.Log.d(tag, String.valueOf(mess), throwable);
        }
    }

    private static <T> void debug(String tag, T[] array, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(array == null){
                    android.util.Log.d(tag, "Array is NULL", throwable);
                }
                else{
                    android.util.Log.d(tag, "Array's length = " + array.length);
                    for(int i = 0; i < array.length; i++){
                        android.util.Log.d(tag, String.format("[%3d] %s", i, String.valueOf(array[i])));
                    }
                }
            }
            else{
                android.util.Log.d(tag, Arrays.toString(array), throwable);
            }
        }
    }

    private static <T> void debug(String tag, List<T> list, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(list == null){
                    android.util.Log.d(tag, "List is NULL", throwable);
                }
                else{
                    android.util.Log.d(tag, "List's size = " + list.size());
                    int l = list.size();
                    for(int i = 0; i < l; i++){
                        android.util.Log.d(tag, String.format("[%3d] %s", i, String.valueOf(list.get(i))));
                    }
                }
            }
            else{
                android.util.Log.d(tag, Arrays.toString(list.toArray()), throwable);
            }
        }
    }

    public static<T> void d(String message){
        debug(null, message, null);
    }

    public static<T> void d(String tag, String message){
        debug(tag, message, null);
    }

    public static<T> void d(String message, Throwable throwable){
        debug(null, message, throwable);
    }

    public static<T> void d(String tag, String message, Throwable throwable){
        debug(tag, message, throwable);
    }

    public static<T> void d(T[] array){
        debug(null, array, true, null);
    }

    public static<T> void d(T[] array, boolean lines){
        debug(null, array, lines, null);
    }

    public static<T> void d(String tag, T[] array){
        debug(tag, array, true, null);
    }

    public static<T> void d(String tag, T[] array, boolean lines){
        debug(tag, array, lines, null);
    }

    public static<T> void d(T[] array, Throwable throwable){
        debug(null, array, true, throwable);
    }

    public static<T> void d(String tag, T[] array, Throwable throwable){
        debug(tag, array, true, throwable);
    }

    public static<T> void d(T[] array, boolean lines, Throwable throwable){
        debug(null, array, lines, throwable);
    }

    public static<T> void d(String tag, T[] array, boolean lines, Throwable throwable){
        debug(tag, array, lines, throwable);
    }

    public static<T> void d(List<T> list){
        debug(null, list, true, null);
    }

    public static<T> void d(List<T> list, boolean lines){
        debug(null, list, lines, null);
    }

    public static<T> void d(String tag, List<T> list){
        debug(tag, list, true, null);
    }

    public static<T> void d(String tag, List<T> list, boolean lines){
        debug(tag, list, lines, null);
    }

    public static<T> void d(List<T> list, Throwable throwable){
        debug(null, list, true, throwable);
    }

    public static<T> void d(String tag, List<T> list, Throwable throwable){
        debug(tag, list, true, throwable);
    }

    public static<T> void d(List<T> list, boolean lines, Throwable throwable){
        debug(null, list, lines, throwable);
    }

    public static<T> void d(String tag, List<T> list, boolean lines, Throwable throwable){
        debug(tag, list, lines, throwable);
    }


    private static <T>void verbose(String tag, T mess, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            android.util.Log.v(tag, String.valueOf(mess), throwable);
        }
    }

    private static <T> void verbose(String tag, T[] array, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(array == null){
                    android.util.Log.v(tag, "Array is NULL", throwable);
                }
                else{
                    android.util.Log.v(tag, "Array's length = " + array.length);
                    for(int i = 0; i < array.length; i++){
                        android.util.Log.v(tag, String.format("[%3d] %s", i, String.valueOf(array[i])));
                    }
                }
            }
            else{
                android.util.Log.v(tag, Arrays.toString(array), throwable);
            }
        }
    }

    private static <T> void verbose(String tag, List<T> list, boolean lines, Throwable throwable){
        if(DEBUG){
            tag = tagToString(tag);
            if(lines){
                if(list == null){
                    android.util.Log.v(tag, "List is NULL", throwable);
                }
                else{
                    android.util.Log.v(tag, "List's size = " + list.size());
                    int l = list.size();
                    for(int i = 0; i < l; i++){
                        android.util.Log.v(tag, String.format("[%3d] %s", i, String.valueOf(list.get(i))));
                    }
                }
            }
            else{
                android.util.Log.v(tag, Arrays.toString(list.toArray()), throwable);
            }
        }
    }

    public static<T> void v(String message){
        verbose(null, message, null);
    }

    public static<T> void v(String tag, String message){
        verbose(tag, message, null);
    }

    public static<T> void v(String message, Throwable throwable){
        verbose(null, message, throwable);
    }

    public static<T> void v(String tag, String message, Throwable throwable){
        verbose(tag, message, throwable);
    }

    public static<T> void v(T[] array){
        verbose(null, array, true, null);
    }

    public static<T> void v(String tag, T[] array){
        verbose(tag, array, true, null);
    }

    public static<T> void v(T[] array, Throwable throwable){
        verbose(null, array, true, throwable);
    }

    public static<T> void v(String tag, T[] array, Throwable throwable){
        verbose(tag, array, true, throwable);
    }

    public static<T> void v(T[] array, boolean lines, Throwable throwable){
        verbose(null, array, lines, throwable);
    }

    public static<T> void v(String tag, T[] array, boolean lines, Throwable throwable){
        verbose(tag, array, lines, throwable);
    }

    public static<T> void v(List<T> list){
        verbose(null, list, true, null);
    }

    public static<T> void v(List<T> list, boolean lines){
        verbose(null, list, lines, null);
    }

    public static<T> void v(String tag, List<T> list){
        verbose(tag, list, true, null);
    }

    public static<T> void v(String tag, List<T> list, boolean lines){
        verbose(tag, list, lines, null);
    }

    public static<T> void v(List<T> list, Throwable throwable){
        verbose(null, list, true, throwable);
    }

    public static<T> void v(String tag, List<T> list, Throwable throwable){
        verbose(tag, list, true, throwable);
    }

    public static<T> void v(List<T> list, boolean lines, Throwable throwable){
        verbose(null, list, lines, throwable);
    }

    public static<T> void v(String tag, List<T> list, boolean lines, Throwable throwable){
        verbose(tag, list, lines, throwable);
    }
}
