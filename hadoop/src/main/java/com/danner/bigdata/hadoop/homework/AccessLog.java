package com.danner.bigdata.hadoop.homework;

public class AccessLog {
    private String user;  //用户账号，可能为null
    private String platform; // 操作系统
    private String version;  //软件版本号
    private String ip;  // ==> 经纬度  省份/城市/运营商/构建标签/构建商圈
    private String traffic;
    private String time;
    private String duration;
    private String appId; // 一家公司可能有多个app

    private long size;  // 流量

    private String y;
    private String m;
    private String d;

    private String isp;
    private String province;
    private String city;

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return  user +  "\t"  + platform + "\t"  + version + "\t" +ip + "\t" + size + "\t" +
                time + "\t" + duration  + "\t" + appId  + "\t" + province + "\t" + city +"\t" +
                isp +"\t" +  y + "\t" + m + "\t" + d ;

    }
}
