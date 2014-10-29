package com.tuanchauict.log; //<-- change it if you want

import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tuanchauict on 8/5/14.
 */
public class LOG {
    public static boolean DEBUG = true;

    private boolean debug = DEBUG;
    private boolean fullname;


    public LOG(){
        this(DEBUG, false);
    }

    public LOG(boolean debug){
        this(debug, false);
    }

    public LOG(boolean debug, boolean fullname){
        this.debug = debug;
        this.fullname = fullname;
    }

    public void on(){
        this.debug = true;
    }

    public void off(){
        this.debug = false;
    }

    private String getStacktrace(){
        StackTraceElement[] es = Thread.currentThread().getStackTrace();
        StackTraceElement caller = es[4];
        String filename = caller.getFileName();
        if(!fullname){
            int index = filename.lastIndexOf("/") + 1;
            filename = filename.substring(index).replace(".java", "");

        }
        return filename + ":"+ caller.getLineNumber();
    }

    public void here(){
        if(debug){
            Log.i(getStacktrace(), "here");
        }
    }

    public void wtf(){
        Log.w(getStacktrace(), "WHAT THE FUCK!!!!!!!!!!!");
    }

    public void e(String str){
        if(debug){
            Log.e(getStacktrace(), str);
        }
    }

    public void e(String str, Throwable throwable){
        if(debug){
            Log.e(getStacktrace(), str, throwable);
        }
    }

    public void e(Object[] arr){
        if(debug){
            Log.e(getStacktrace(), Arrays.toString(arr));
        }
    }

    public void e(Object[] arr, Throwable throwable){
        if(debug){
            Log.e(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public void e(List<Object> list){
        if(debug){
            Log.e(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public void e(List<Object> list, Throwable throwable){
        if(debug){
            Log.e(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public void w(String str){
        if(debug){
            Log.w(getStacktrace(), str);
        }
    }

    public void w(String str, Throwable throwable){
        if(debug){
            Log.w(getStacktrace(), str, throwable);
        }
    }

    public void w(Object[] arr){
        if(debug){
            Log.w(getStacktrace(), Arrays.toString(arr));
        }
    }

    public void w(Object[] arr, Throwable throwable){
        if(debug){
            Log.w(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public void w(List<Object> list){
        if(debug){
            Log.w(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public void w(List<Object> list, Throwable throwable){
        if(debug){
            Log.w(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public void i(String str){
        if(debug){
            Log.i(getStacktrace(), str);
        }
    }

    public void i(String str, Throwable throwable){
        if(debug){
            Log.i(getStacktrace(), str, throwable);
        }
    }

    public <T>void i(T[] arr){
        if(debug){
            Log.i(getStacktrace(), Arrays.toString(arr));
        }
    }

    public <T>void i(T[] arr, Throwable throwable){
        if(debug){
            Log.i(getStacktrace(), Arrays.toString(arr), throwable);
        }
    }

    public <T> void i(List<T> list){
        if(debug){
            Log.i(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public <T> void i(List<T> list, Throwable throwable){
        if(debug){
            Log.i(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public void d(String str){
        if(debug){
            Log.d(getStacktrace(), str);
        }
    }

    public void d(String str, Throwable throwable){
        if(debug){
            Log.d(getStacktrace(), str, throwable);
        }
    }

    public void d(Object[] arr){
        if(debug){
            Log.d(getStacktrace(), Arrays.toString(arr));
        }
    }

    public void d(Object[] arr, Throwable throwable){
        if(debug){
            Log.d(getStacktrace(), Arrays.toString(arr),  throwable);
        }
    }

    public void d(List<Object> list){
        if(debug){
            Log.d(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public void d(List<Object> list, Throwable throwable){
        if(debug){
            Log.d(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }

    public void v(String str){
        if(debug){
            Log.v(getStacktrace(), str);
        }
    }

    public void v(String str, Throwable throwable){
        if(debug){
            Log.v(getStacktrace(), str,  throwable);
        }
    }

    public void v(Object[] arr){
        if(debug){
            Log.v(getStacktrace(), Arrays.toString(arr));
        }
    }

    public void v(Object[] arr, Throwable throwable){
        if(debug){
            Log.v(getStacktrace(), Arrays.toString(arr));
        }
    }

    public void v(List<Object> list){
        if(debug){
            Log.v(getStacktrace(), Arrays.toString(list.toArray()));
        }
    }

    public void v(List<Object> list, Throwable throwable){
        if(debug){
            Log.v(getStacktrace(), Arrays.toString(list.toArray()), throwable);
        }
    }
}
