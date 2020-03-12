package com.danner.bigdata.hadoop.homework;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * action 生成器
 */
public class GeneratorAction {

    private static Random mRandom = new Random();
    private static String[] userList = new String[]{"li","wang","zhang","zhao","john","tom","wu","--"};
    private static String[] versionList = new String[]{"1.0","2.0","3.0","4.0","5.0"};
    private static String[] appIdList = new String[]{"comId","orgId","wwwId","httpId","qyId","webId"};
    private static int[][] ipRange = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
            { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
            { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
            { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
            { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
            { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
            { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
            { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
            { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
            { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
    };

    private static String URL = "http://danner000:80/log/sendAction";

    public static void main(String[] args) throws Exception {
        while (true){
//            sendAction();
            System.out.println(getUser() + "\t"+ getPlatform() + "\t" + getVersion() + "\t" + getIp() +
                "\t" + getTraffic() + "\t" + getTime() + "\t" + getDuration() + "\t" + getAppId());
            Thread.sleep(20);
        }
    }
    public static void sendAction() throws Exception {
        JSONObject json = getJson();
        System.out.println(json.toString());
//        UploadUtils.upload(json.toString(),URL);
//        HttpUtil.postJson(URL,json.toString());
    }

    public static JSONObject getJson(){
        JSONObject js = new JSONObject();
        js.put("user",getUser());
        js.put("platform",getPlatform());
        js.put("version",getVersion());
        js.put("ip",getIp());
        js.put("traffic",getTraffic());
        js.put("time",getTime());
        js.put("duration",getDuration());
        js.put("appId",getAppId());
        return js;
    }
    public static String getUser(){
        return userList[mRandom.nextInt(userList.length)];
    }
    public static String getPlatform(){
        return mRandom.nextInt(2) == 1 ? "Android" : "iOS";
    }
    public static String getVersion(){
        return versionList[mRandom.nextInt(versionList.length)];
    }
    public static String getIp(){
        int index = mRandom.nextInt(ipRange.length);
        String ip = num2ip(ipRange[index][0] + new Random().nextInt(ipRange[index][1] - ipRange[index][0]));
        return ip;
    }
    public static String getTraffic(){
        if(mRandom.nextInt(5) == 3){
            return "-";
        }else{
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<4;i++){
                sb.append(mRandom.nextInt(10));
            }
            return sb.toString();
        }
    }
    public static String getTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(date);
    }
    public static String getDuration(){
        // unit s
        return mRandom.nextInt(300) + 100 +"";
    }
    public static String getAppId(){
        return appIdList[mRandom.nextInt(appIdList.length)];
    }
    /*
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }

}
