package com.danner.java.utils;

import java.util.List;

public class IpUtil {
    /**
     * 二分查找 ip的信息
     *
     * @param ipInfoList
     * @param ip
     * @return
     */
    public static IpInfo searchIp(List<IpInfo> ipInfoList, String ip) {
        //获取到ip的十进制的值
        Long ipLong = IpConvertToInt(ip);
        //初始化左侧索引
        int leftIndex = 0;
        //初始化右侧索引
        int rightIndex = ipInfoList.size() - 1;
        while (leftIndex <= rightIndex) {
            //计算中间索引
            int mid = (leftIndex + rightIndex) >>> 1;
            //获取到中间的对象
            IpInfo ipInfo = ipInfoList.get(mid);
            if (ipLong == ipInfo.getStart()) {
                return ipInfo;
            } else if (ipLong < ipInfo.getStart()) {
                rightIndex = mid - 1;//计算右侧索引
            } else {
                if (ipLong < ipInfo.getEnd()) {
                    return ipInfo;
                }
                leftIndex = mid + 1;
            }
        }
        System.out.println(ip);
        return null;
    }

    /**
     * 把ip转换成十进制
     *
     * @param ip
     * @return
     */
    public static Long IpConvertToInt(String ip) {
        String[] ipStr = ip.split("\\.");
        Long result = 0L;
        int j;
        int i;
        for (i = ipStr.length - 1, j = 0; i >= 0; i--, j++) {
            Long temp = Long.parseLong(ipStr[i]);
            temp = temp << (8 * j);
            result = result | temp;
        }
        return result;
    }

    public static class IpInfo {
        private Long start;//开始的十进制数
        private Long end;//结尾的十进制数
        private String province;//省
        private String city;//市
        private String isp;//运营商

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Long getEnd() {
            return end;
        }

        public void setEnd(Long end) {
            this.end = end;
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

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }
    }
}
