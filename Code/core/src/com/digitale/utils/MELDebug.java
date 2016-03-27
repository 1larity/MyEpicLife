package com.digitale.utils;

import com.digitale.myepiclife.MyEpicLife;

/**
 * Created by Rich on 26/03/2016.
 */
public class MELDebug {

    MELDebug(){

    }
    /**
     *
     * @param message The Message to be sent to log
     * @param localDebug Flag to show if class debug is on
     */
    public static void log(String message, boolean localDebug) {
        if(MyEpicLife.DEBUG && localDebug){
            System.out.println(message);
        }
    }
}
