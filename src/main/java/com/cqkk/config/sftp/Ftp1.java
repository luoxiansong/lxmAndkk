package com.cqkk.config.sftp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * ftp
 */
public class Ftp1 {
    private static final Log log = LogFactory.getLog(Ftp1.class);
    FTPClient ftpClient;

    //连接服务器并登录
    public FTPClient connect(String hostname, Integer port, String username, String password) throws IOException {
        // 创建连接
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(hostname, port);
        // 设置用于FTP控制连接的编码：UTF-8等
        ftpClient.setControlEncoding("UTF-8");
        // 登录服务器
        ftpClient.login(username, password);
        //返回登录结果状态
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            return null;
        }
        log.info("login successful");
        return ftpClient;
    }

    //退出并断开连接
    public void disconnect(FTPClient ftpClient) throws IOException {
        ftpClient.logout();
        log.error("disLogin=>" + ftpClient.getReplyString());
        ftpClient.disconnect();
        log.error("disConnect=>" + ftpClient.getReplyString());
    }

    //获取并下载文件关键代码
    private void download(String remoteFilePath, String tmpFileName, String localFilePath, String newFileName) throws IOException {
        // 进入文件所在远程目录
        ftpClient.changeWorkingDirectory(remoteFilePath);
        // 开启被动模式
        ftpClient.enterLocalPassiveMode();
        // 设置以二进制方式传输
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        // 获取文件，listFiles参数为空时获取当前目录下所有文件，包含文件名时，可通过files.length == 1判断是否存在该文件
        FTPFile[] files = ftpClient.listFiles(tmpFileName);
        for (FTPFile file : files) {
            if (tmpFileName.equals(file.getName())) {
                File newFile = new File(localFilePath + File.separator + newFileName);
                FileOutputStream outputStream = new FileOutputStream(newFile);
                ftpClient.retrieveFile(file.getName(), outputStream);
                outputStream.close();
            }
        }
        ftpClient.disconnect();
        log.info("文件下载成功");
    }

    //获取并上传文件关键代码
    private void upload(String filePath, String uploadPath, String fileName) throws IOException {
        // 创建输入流
        FileInputStream fis = new FileInputStream(filePath);//包含文件名  /home/ap/lkk.txt
        // 进入文件所在FTP远程目录
        ftpClient.changeWorkingDirectory(uploadPath);
        // 开启被动模式
        ftpClient.enterLocalPassiveMode();
        // 调整ftp传输模式为二进制方式
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        // 调用ftp的方法上载
        // 从给定InputStream中获取输入并以给定文件名fileName将文件保存在FTP服务器上。
        boolean ret = ftpClient.storeFile(fileName, fis);
        // 关闭文件流
        fis.close();
    }
}
