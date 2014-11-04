package com.tuanchauict.log;

import com.tuanchauict.log.Log;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tuanchauict on 11/4/14 06:49.
 * com.opla.manga
 */
public class LogTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testTracking(){
        Log.startTrack("Long task");
        for(int i = 0; i < 10; i++){
            try{
                Thread.sleep(100);
                Log.printTrack("Long task");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        Log.stopTrack("Long task");
        Log.printTrack("Long task");
    }

    public void testLog(){
        Log.i("Blah blah");
        Log.i(new String[]{"Blah 0", "Blah 1"});
        Log.i(Arrays.asList(new String[]{"Blag 0", "Blah 1"}));
    }
}
