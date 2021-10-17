package com.cqkk.util;


import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @program: lxmAndkk
 * @description: 文件工具类
 * @author: luo kk
 * @create: 2021-06-03 21:42
 */
public class XmkkFileUtils {

    @Value("${file.upload.path}")
    public String local_upload_path;

    @Value("$(file.download.path)")
    public String Local_download_path;

    public static void main(String[] args) throws IOException {
//        System.out.printf(String.format("%11s", "日期:"));
        create_file();
    }

    /*按照指定格式生成文件*/
    public static boolean create_file() throws IOException {

        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String file_name = "lkk_" + date + "_lxm" + ".txt";

        File file = new File("D:/developmentEnvironment/File/file_upload/" + File.separator + file_name);
        if (!file.exists()) {
            file.createNewFile();
        }
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(String.format("%11s", "日期:") + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + String.format("%66s", "查询流水号:"));
        arrayList.add(String.format("%11s", "日期:"));
        arrayList.add(String.format("%60s", "单一窗口单笔业务查询"));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        for (String line : arrayList) {
            while (!line.isEmpty()) {
                writer.write(line);
                writer.newLine();
                line = "";
            }
        }
        writer.close();
        return true;
    }


}