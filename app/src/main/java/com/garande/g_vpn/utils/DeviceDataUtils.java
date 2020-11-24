package com.garande.g_vpn.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.garande.g_vpn.model.DeviceInfo;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/***
 * @author garande
 * Ref: 'https://www.thinbug.com/q/3213205'
 *      'https://developer.android.com/reference/android/os/Build.html'
 * Created At: 20-11-2020 01:20AM
 */

public class DeviceDataUtils {
    DeviceInfo deviceInfo;

    public DeviceInfo getDeviceInfo(Context context) {
        if (deviceInfo != null) {
            return deviceInfo;
        }
        this.deviceInfo = new DeviceInfo();
        deviceInfo.LANGUAGE = Locale.getDefault().getDisplayLanguage();
        deviceInfo.TIME_ZONE = TimeZone.getDefault().getID();
        deviceInfo.LOCAL_COUNTRY_CODE = context.getResources().getConfiguration().locale.getCountry();
        deviceInfo.TOTAL_MEMORY = String.valueOf(getTotalMemory(context));
        deviceInfo.USED_MEMORY = String.valueOf(getUsedMemory(context));
        deviceInfo.FREE_MEMORY = String.valueOf(getFreeMemory(context));
        deviceInfo.MAC_ADDRESS = getDeviceMac(context);
        deviceInfo.TOTAL_CPU_USAGE = getDeviceCpuUsage();
        deviceInfo.TOTAL_CPU_USAGE_SYSTEM = getDeviceCpuUsageSystem();
        deviceInfo.TOTAL_CPU_USAGE_USER = getDeviceCpuUsageUser();
        deviceInfo.TOTAL_CPU_IDLE = getDeviceCpuUsageIdle();
        deviceInfo.SCREEN_SIZE_IN_INCH = getDeviceSizeInInch(context);
        deviceInfo.NETWORK_TYPE = getNetworkType(context);
        deviceInfo.NETWORK = getNetworkStatus(context);
        deviceInfo.TYPE = getDeviceType(context);
        deviceInfo.ID = getDeviceId(context);
        deviceInfo.IP_ADDRESS = getIpAddress(true);
        return deviceInfo;
    }

    private String getDeviceId(Context context) {
        String device_uuid = "";
        try {
            device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (device_uuid == null)
                device_uuid = "123456789";
            else {
                try {
                    byte[] data = device_uuid.getBytes();
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    digest.update(data);
                    data = digest.digest();
                    BigInteger bigInteger = new BigInteger(data).abs();
                    device_uuid = bigInteger.toString(36);
                } catch (Exception e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(DeviceDataUtils.class.getSimpleName(), null, e);
        }

        return device_uuid;
    }

    private String getDeviceType(Context context) {
        if (isTablet(context)) {
            if (isDeviceMoreThan5Inch(context)) {
                return "Tablet";
            } else {
                return "Mobile";
            }
        } else return "Mobile";
    }

    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private boolean isDeviceMoreThan5Inch(Context context) {
        try {
            double diagonalSize = Double.parseDouble(getDeviceSizeInInch(context));
            if (diagonalSize >= 7)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String getNetworkStatus(Context context) {
        String networkStatus = "";
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //check for wifi
            final NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //check for mobile data
            final NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifi.isAvailable())
                networkStatus = "WiFi";
            else if (mobile.isAvailable())
                networkStatus = getDataType(context);
            else {
                networkStatus = "noNetwork";
                networkStatus = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            networkStatus = "0";
        }

        return networkStatus;
    }

    private String getNetworkType(Context context) {
        String networkStatus = "";

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //check for wifi
        final NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //check for mobile data
        final NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isAvailable())
            networkStatus = "WiFi";
        else if (mobile.isAvailable())
            networkStatus = getDataType(context);
        else
            networkStatus = "noNetwork";

        return networkStatus;
    }

    private String getDataType(Context context) {
        String type = "Mobile Data";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type = "Mobile Data 3G";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                type = "Mobile Data 4G";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type = "Mobile Data GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type = "Mobile Data EDGE 2G";
                break;
        }

        return type;
    }


    private String getDeviceSizeInInch(Context activity) {
        try{
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

            float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
            float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
            double diagonalInches = Math.sqrt(xInches * xInches + yInches*yInches);
            return String.valueOf(diagonalInches);
        }catch (Exception e){
            return "-1";
        }
    }

    private String getDeviceCpuUsage() {
        int[] cpuUsage = getCpuUsageStatistic();
        if(cpuUsage != null){
            int total = cpuUsage[0] + cpuUsage[1] + cpuUsage[2] + cpuUsage[3];
            return String.valueOf(total);
        }
        return "";
    }

    private String getDeviceCpuUsageIdle() {
        int[] cpuUsage = getCpuUsageStatistic();
        if(cpuUsage != null){
            int total = cpuUsage[2];
            return String.valueOf(total);
        }
        return "";
    }

    private String getDeviceCpuUsageSystem() {
        int[] cpuUsage = getCpuUsageStatistic();
        if(cpuUsage != null){
            int total = cpuUsage[1];
            return String.valueOf(total);
        }
        return "";
    }
    private String getDeviceCpuUsageUser() {
        int[] cpuUsage = getCpuUsageStatistic();
        if(cpuUsage != null){
            int total = cpuUsage[0];
            return String.valueOf(total);
        }
        return "";
    }

    /**
     * returns integer Array with 4 elements: user, system, idle and other cpu usage in percentage
     * @return
     */
    private int[] getCpuUsageStatistic() {
        try {
            String temp = executeTop();

            temp = temp.replaceAll(",", "");
            temp = temp.replaceAll("User", "");
            temp = temp.replaceAll("System", "");
            temp = temp.replaceAll("IOW", "");
            temp = temp.replaceAll("IRQ", "");
            temp = temp.replaceAll("%", "");
            for (int i = 0; i < 10; i++){
                temp = temp.replaceAll("  ", " ");
            }
            temp = temp.trim();
            String[] myString = temp.split(" ");
            int[] cpuUsageAsInt = new int[myString.length];
            for (int i = 0; i < myString.length; i++){
                myString[i] = myString[i].trim();
                cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
            }
            return cpuUsageAsInt;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String executeTop() {
        Process process = null;
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            process = Runtime.getRuntime().exec("top -n 1");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (line == null || line.contentEquals("")){
                line = bufferedReader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
                process.destroy();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return line;
    }


    private String getIpAddress(boolean useIPv4){
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces){
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : addresses){
                    if (!inetAddress.isLoopbackAddress()){
                        String addressString = inetAddress.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(addressString);
                        if(useIPv4){
                            if(isIPv4) return addressString;
                        }else {
                            if(!isIPv4){
                                int delim = addressString.indexOf('%');//drop ip6 port
                                return delim < 0 ? addressString : addressString.substring(0, delim);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String getDeviceMac(Context context) {
        String mac = getMacAddressFromInterface("wlan0");
        if(TextUtils.isEmpty(mac)){
            mac = getMacAddressFromInterface("eth0");
        }
        if (TextUtils.isEmpty(mac)){
            mac = "DU:MM:YA:DD:RE:SS";
        }

        return mac;
    }

    private String getMacAddressFromInterface(String interfaceName) {
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces){
                if(interfaceName != null){
                    if(!networkInterface.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = networkInterface.getHardwareAddress();
                if(mac == null)
                    return "";
                StringBuilder stringBuilder = new StringBuilder();
                for (int index = 0; index < mac.length; index++)
                    stringBuilder.append(String.format("%02X:", mac[index]));
                if(stringBuilder.length() > 0)
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                return stringBuilder.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
        return "";
    }


    public long getTotalMemory(Context activity){
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(memoryInfo);
            long totalMemoryInMbs = memoryInfo.totalMem / 1048576L;
            return totalMemoryInMbs;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public long getFreeMemory(Context activity){
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(memoryInfo);
            long totalMemoryInMbs = memoryInfo.availMem / 1048576L;
            return totalMemoryInMbs;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public long getUsedMemory(Context activity){
        return (getTotalMemory(activity) - getFreeMemory(activity));
    }
}
