package com.android.ubclaunchpad.driver.util;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by kelvinchan on 2016-06-10.
 * The metric we will use to determine faster phone processor is BogoMIPS
 */
public class HardwareUtils {

    public static Map<String, String> getCpuInfoMap() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Scanner s = new Scanner(new File("/proc/cpuinfo"));
            while (s.hasNextLine()) {
                String[] vals = s.nextLine().split(": ");
                if (vals.length > 1)
                    map.put(vals[0].trim(), vals[1].trim());
            }
        } catch (Exception e) {
            Log.e("getCpuInfoMap", Log.getStackTraceString(e));}
        return map;
    }

//    public static double getBogoMIPS(){
//        Map<String, String> cpuInfo = getCpuInfoMap();
//        if(cpuInfo.size() >= 0 && cpuInfo.containsKey("BogoMIPS")){
//            try {
//                double mips = Double.parseDouble(cpuInfo.get("BogoMIPS"));
//                return mips;
//            }
//            catch (NullPointerException){
//
//            }
//            catch (NumberFormatException){
//
//            }
//        }
//        return
//    }
}
