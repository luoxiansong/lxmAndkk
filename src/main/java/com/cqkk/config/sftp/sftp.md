JSch是Java Secure Channel的缩写。JSch是一个SSH2的纯Java实现。
它允许你连接到一个SSH服务器，并且可以使用端口转发，X11转发，文件传输等，当然你也可以集成它的功能到你自己的应用程序。

本文只介绍如何使用JSch实现的SFTP功能。

SFTP是Secure File Transfer Protocol的缩写，安全文件传送协议。
可以为传输文件提供一种安全的加密方法。SFTP 为 SSH的一部份，是一种传输文件到服务器的安全方式。
SFTP是使用加密传输认证信息和传输的数据，所以，使用SFTP是非常安全的。
但是，由于这种传输方式使用了加密/解密技术，所以传输效率比普通的FTP要低得多，
如果您对网络安全性要求更高时，可以使用SFTP代替FTP。

要使用JSch，需要下载它的jar包，请从官网下载它：http://www.jcraft.com/jsch/

ChannelSftp类是JSch实现SFTP核心类，它包含了所有SFTP的方法，如：
put()：      文件上传
get()：      文件下载
cd()：       进入指定目录
ls()：       得到指定目录下的文件列表
rename()：   重命名指定文件或目录
rm()：       删除指定文件
mkdir()：    创建目录
rmdir()：    删除目录
等等（这里省略了方法的参数，put和get都有多个重载方法，具体请看源代码，这里不一一列出。）

JSch支持三种文件传输模式：
OVERWRITE
完全覆盖模式，这是JSch的默认文件传输模式，即如果目标文件已经存在，传输的文件将完全覆盖目标文件，产生新的文件。
RESUME
恢复模式，如果文件已经传输一部分，这时由于网络或其他任何原因导致文件传输中断，如果下一次传输相同的文件，
则会从上一次中断的地方续传。
APPEND
追加模式，如果目标文件已存在，传输的文件将在目标文件后追加。

#文件上传
实现文件上传可以调用ChannelSftp对象的put方法。ChannelSftp中有12个put方法的重载方法：
public void put(String src, String dst)
将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
采用默认的传输模式：OVERWRITE

public void put(String src, String dst, int mode)
将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
指定文件传输模式为mode（mode可选值为：ChannelSftp.OVERWRITE，ChannelSftp.RESUME，
ChannelSftp.APPEND）

public void put(String src, String dst, SftpProgressMonitor monitor)
将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
采用默认的传输模式：OVERWRITE
并使用实现了SftpProgressMonitor接口的monitor对象来监控文件传输的进度。

public void put(String src, String dst, SftpProgressMonitor monitor, int mode)
将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
指定传输模式为mode
并使用实现了SftpProgressMonitor接口的monitor对象来监控文件传输的进度。

public void put(InputStream src, String dst)
将本地的input stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。
采用默认的传输模式：OVERWRITE

public void put(InputStream src, String dst, int mode)
将本地的input stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。
指定文件传输模式为mode

public void put(InputStream src, String dst, SftpProgressMonitor monitor)
将本地的input stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。
采用默认的传输模式：OVERWRITE
并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。

public void put(InputStream src, String dst, SftpProgressMonitor monitor, int mode)
将本地的input stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。
指定文件传输模式为mode
并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。

public OutputStream put(String dst)
该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。
采用默认的传输模式：OVERWRITE

public OutputStream put(String dst, final int mode)
该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。
指定文件传输模式为mode

public OutputStream put(String dst, final SftpProgressMonitor monitor, final int mode)
该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。
指定文件传输模式为mode
并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。

public OutputStream put(String dst, final SftpProgressMonitor monitor, final int mode, long offset)
该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。
指定文件传输模式为mode
并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。
offset指定了一个偏移量，从输出流偏移offset开始写入数据。