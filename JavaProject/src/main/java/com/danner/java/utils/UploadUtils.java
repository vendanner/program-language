package com.danner.java.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadUtils {
    public static void upload(String log, String URL) throws Exception {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求方式为POST
        connection.setRequestMethod("POST");
        connection.setRequestProperty("clientTime", System.currentTimeMillis() + "");
        //允许写出
        connection.setDoOutput(true);
        //设置参数类型是json格式
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        OutputStream out = connection.getOutputStream();
        out.write(log.getBytes());
        out.flush();
        out.close();
        int resultCode = connection.getResponseCode();
        System.out.println(resultCode);
    }
}
