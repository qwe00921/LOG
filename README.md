LOG (0.1.2)
===

Android Log wrapper. Make your lovely cat simpler. You can throw out `android.util.Log` with mine or yours (Change the `package name` if you want)

##Change logs:
Version 0.1.2: 
- Add version for LOG. From version 0.1.2, I have written `Log.java` instead of `LOG.java`. **`LOG.java` is Deprecated**
- Change all functions to `static`. Now you don't need to create object for logging.
- Add tracking runtime. See [example](https://github.com/tuanchauict/LOG/blob/master/README.md#tracking-runtime)
- Add `tag` parameter to keep your old Log debug safely
##What new?

##Features
~~Nothing new, but~~ You can save your time finding where the logs were printed and put `tag` into `Log.blah(tag, msg)`

[NEW] Tracking running time for your `tag`

###Example:

Your log is written in `Foo.java`, at line 145:
    
    [145] Log.i("your message here");

output: 

    Foo:145 : your message here

####Tracking runtime?

    Log.startTrack("Long task"); //[START] Tracking runtime for: [Long task]
    ...
    Log.printTrack("Long task"); //[INFO] [Long task] run in 1503ms (1s503ms). Different from previous checking: 100ms (100ms)
    ...
    Log.stopTrack("Long task"); //[STOP] [Long task] run in 2008ms (2s8ms). Different from previous checking: 0ms (0ms)
    Log.printTrack("Long task");//[ERROR] Tracking [Long task] NOT FOUND!
    
##Usage

Download and put Log.java to your Android project

    import <package>.Log

    Log.i("your message or your array or your list here");
    Log.e("something like above, but you can throw an exception", exception);
    Log.wtf(); //just "WHAT THE FUCK!!!!!!!!!!" <-- I know your feeling LOL
    Log.here(); //just "here" <-- I know sometime, you just want this 
    Log.on(); //for start print log
    Log.off(); //for stop print log
    
    //Tracking runtime:
    Log.startTrack("Long task");
    Log.printTrack("Long task");
    Log.stopTrack("Long task");
    
  
##Licence:

This library uses the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Copyright: [tuanchauict](https://github.com/tuanchauict)

  
