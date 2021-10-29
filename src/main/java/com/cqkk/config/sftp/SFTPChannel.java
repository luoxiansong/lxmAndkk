package com.cqkk.config.sftp;

import com.github.pagehelper.util.StringUtil;
import com.jcraft.jsch.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.platform.commons.util.StringUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * JSch-Java实现的SFTP
 */
public class SFTPChannel {
    private static final Log log = LogFactory.getLog(SFTPChannel.class);

    Session session;
    Channel channel;

    String localFile = "/home/zimug/local/random.txt";   //本地文件路径
    String remoteFile = "/home/zimug/remote/targetfile.txt";   //上传到远程的文件路径，要保证登录用户有写权限

    public ChannelSftp getChannel(Map<String, Object> sftpDetails, int timeout) throws JSchException {
        String ftpHost = (String) sftpDetails.get(SFTPConstants.SFTP_REQ_HOST);
        String port = (String) sftpDetails.get(SFTPConstants.SFTP_REQ_PORT);
        String ftpUserName = (String) sftpDetails.get(SFTPConstants.SFTP_REQ_USERNAME);
        String ftpPassword = (String) sftpDetails.get(SFTPConstants.SFTP_REQ_PASSWORD);

        int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
        if (!StringUtil.isEmpty(port)) {
            ftpPort = Integer.parseInt(port);
        }

        JSch jSch = new JSch();// 创建JSch对象
        session = jSch.getSession(ftpUserName, ftpHost, ftpPort);// 根据用户名，主机ip，端口获取一个Session对象
        log.debug("session create.");
        if (StringUtil.isEmpty(ftpPassword)) {
            session.setPassword(ftpPassword);// 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);//为session对象设置properties
        session.setTimeout(timeout);//设置timeout时间
        session.connect();//创建链接
        log.debug("session connected.");

        log.debug("open channel.");
        channel = session.openChannel("sftp");//打开SFTP通道
        channel.connect();//简历SFTP通道的链接
        log.debug("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
                + ", returning: " + channel);
        return (ChannelSftp) channel;
    }

    private void closeChannel() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * 上传
     */
    private void upload() throws JSchException {
        HashMap<String, Object> map = new HashMap<>();
        // 传输本地文件到远程主机
        try {
            getChannel(map, 1000).put(localFile, remoteFile);
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        System.out.println("文件传输完成！");
    }

    /**
     * 上传文件
     */
  /*  public void upload(String directory, String uploadFile, SftpConfig sftpConfig) {
        ChannelSftp sftp = connect(sftpConfig);
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            try {
                sftp.mkdir(directory);
                sftp.cd(directory);
            } catch (SftpException e1) {
                throw new RuntimeException("ftp创建文件路径失败" + directory);
            }
        }
        File file = new File(uploadFile);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            sftp.put(inputStream, file.getName());
        } catch (Exception e) {
            throw new RuntimeException("sftp异常" + e);
        } finally {
            disConnect(sftp);
            closeStream(inputStream, null);
        }
    }*/
}
