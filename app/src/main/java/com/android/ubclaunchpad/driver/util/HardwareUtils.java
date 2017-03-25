package com.android.ubclaunchpad.driver.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

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
// TODO remove this method - it is only used as an example for now
//    /**
//     * Determines the BogoMIPS based on proc/cpuinfo from the phones stats
//     * @return The numeric BogoMIPS value. Zero if no MIPS found
//     */
//    public static double getBogoMIPS(){
//        Map<String, String> cpuInfo = getCpuInfoMap();
//        if(cpuInfo.size() >= 0 && cpuInfo.containsKey("BogoMIPS")){
//            try {
//                double mips = Double.parseDouble(cpuInfo.get("BogoMIPS"));
//                return mips;
//            }
//            catch (NullPointerException e){
//                Log.e("Hardware Util", "Null error  " + e.getMessage());
//            }
//            catch (NumberFormatException e){
//                Log.e("Hardware Util", "Number format error " + e.getMessage());
//            }
//        }
//        return 0; //default return 0
//    }

    /**
     * Determines the numeric value based on proc/cpuinfo from the phones stats
     * This method only works for fields that return a number value such as
     * "BogoMIPS" and "Processor"
     * @return The numeric BogoMIPS value. Zero if no MIPS found
     */
    public static double getPhoneValue(Map<String, String> cpuInfo, String infoKey){
        if(cpuInfo.size() >= 0 && cpuInfo.containsKey(infoKey)){
            try {
                double infoValue = Double.parseDouble(cpuInfo.get(infoKey));
                return infoValue;
            }
            catch (NullPointerException e){
                Log.e("Hardware Util", "Null " + e.getMessage());
            }
            catch (NumberFormatException e){
                Log.e("Hardware Util", "Number format " + e.getMessage());
            }
            return 0; //default return 0
        }
        return 0;
    }

    /**
     * Compares the stats of two devices and returns true if "myPhone" is faster than the "otherPhone"
     * "Faster" in this context is based on the BogoMIPS and the higher is considered faster
     * By arbitrary reasons, if BogoMIPS == 0, the # of processor * 5.5 will be the BogoMIPS value used
     * @param myPhone cpuInfo of this device
     * @param otherPhone cpuInfo of other device
     * @return true if myPhone BogoMIPS > otherPhone BogoMIPS
     */
    public static boolean isPhoneFaster(Map<String, String> myPhone, Map<String, String> otherPhone){

        double myPhoneProduct = getPhoneValue(myPhone, "BogoMIPS");
        double otherPhoneProduct = getPhoneValue(otherPhone, "BogoMIPS");

        if (myPhoneProduct == 0) {
            myPhoneProduct = 5.5 * getPhoneValue(myPhone, "processor");
        }
        if (otherPhoneProduct == 0) {
            otherPhoneProduct = 5.5 * getPhoneValue(otherPhone, "processor");
        }
        return (myPhoneProduct >= otherPhoneProduct);
    }

    public static LatLng getGPS(Context context){

//        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            return new LatLng(latitude, longitude);
//        } else {
            return new LatLng(0, 0);
       // }
    }
}
