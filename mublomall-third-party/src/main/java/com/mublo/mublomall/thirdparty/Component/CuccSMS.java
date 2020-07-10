/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-08 13:39
 */

package com.mublo.mublomall.thirdparty.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: mublo
 * @Date: 2020/7/8 13:39
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
//动态获取配置中心配置内容
//@RefreshScope
@Component
public class CuccSMS {
    private static String usr;
    private static String password;
    @Value("${SMSUSERNAME}")
    public void setUsr(String SMSUSERNAME) {
        CuccSMS.usr = SMSUSERNAME;
    }
    @Value("${SMSPASSWORD}")
    public void setPassword(String SMSPASSWORD) {
        CuccSMS.password = SMSPASSWORD;
    }

    /**
     * 联通短信发送
     *
     * @param mobile 接受手机号,多个号码以逗号分隔如"13515714339,18823178320" ,最多20个号码
     * @param sms    发送内容包含签名(签名需要跟账号的签名一致)
     */
    public static void sendSMS(String mobile, String sms, int type) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String text = usr + "|" + password + "|" + mobile;
            String sign = md5LowerCase(text);
            stringBuffer.append("usr=" + usr);
            stringBuffer.append("&mobile=" + mobile);
            sms = URLEncoder.encode(sms, "GBK");
            stringBuffer.append("&sms=" + sms);
            stringBuffer.append("&type=" + type);
            stringBuffer.append("&extdsrcid=");
            stringBuffer.append("&sign=" + sign);
            sendPost("http://xapi.hzsysms.com:6088", stringBuffer.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static final String md5LowerCase(String src) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(src.getBytes("UTF-8"));
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    public static String sendPost(String url, String param) {
        if (url == null) {
            return null;
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
//		System.out.println("Send to " + url + "?" + param);
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(8000);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            out.close();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
//			System.out.println("http send error: " + e.getMessage());
            result = null;
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
        return result;
    }
}