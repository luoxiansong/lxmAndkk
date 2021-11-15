package com.cqkk.config.HTTP;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;

public class HttpURLConnectionTest {
    //介绍HttpClient库的使用前，先介绍jdk里HttpURLConnection，因为HttpClient是开源的第三方库，使用方便，不过jdk里的都是比较基本的，
    // 有时候没有HttpClient的时候也可以使用jdk里的HttpURLConnection，HttpURLConnection都是调jdk java.net库的，
    // 下面给出实例代码:
    public static void main(String[] args) throws Exception {
        String url = "https://ocr-api.ccint.com/ocr_service?app_key=%s";
        String appKey = "xxxxxx"; // your app_key
        String appSecret = "xxxxxx"; // your app_secret
        url = String.format(url, appKey);
        OutputStreamWriter out = null;
        InputStream in = null;
        String result = "";
        try {
            String imgData = imageToBase64("src/main/java/com/cqkk/config/HTTP/img.png");
            String param = "{\"app_secret\":\"%s\",\"image_data\":\"%s\"}";
            param = String.format(param, appSecret, imgData);
            URL realUrl = new URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.append(param);
            out.flush();
            out.close();
            int rc = conn.getResponseCode();
            System.out.println(("connect result is:" + rc));
            // 响应成功
            if (rc == 200) {
                in = conn.getInputStream();
                BufferedReader data = new BufferedReader(new InputStreamReader(
                        in, "utf-8"));
                StringBuffer result1 = new StringBuffer();
                String temp;
                while ((temp = data.readLine()) != null) {
                    result1.append(temp);
                }
                System.out.println(result1);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
    }

    public static String imageToBase64(String path) {
        String imgFile = path;
        InputStream in;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}