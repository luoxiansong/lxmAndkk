package com.cqkk.config.socket.socket_2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 1122;
    private ServerSocket serverSocket;

    public Server() throws Exception {
        serverSocket = new ServerSocket(port, 3);
        System.out.println("服务器启动!");
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("New connection accepted " +
                        socket.getInetAddress() + ":" + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        Thread.sleep(60000 * 10);
        server.service();
    }
}
//  Client 试图与 Server 进行 100 次连接. 在 Server 类中, 把连接请求队列的长度设为 3. 这意味着当队列中有了 3 个连接请求时,
//  如果Client 再请求连接, 就会被 Server 拒绝.  下面按照以下步骤运行 Server 和 Client 程序.
//(1)在Server 中只创建一个 ServerSocket 对象, 在构造方法中指定监听的端口为1122 和 连接请求队列的长度为 3 . 构造 Server 对象后, Server 程序睡眠 10 分钟, 并且在 Server 中不执行 serverSocket.accept() 方法. 这意味着队列中的连接请求永远不会被取出. 运行Server 程序和 Client 程序后, Client程序的打印结果如下:
//第 1 次连接成功
//第 2 次连接成功
//第 3 次连接成功
//Exception in thread “main” java.net.ConnectException: Connection refused: connect
//…………….
//从以上打印的结果可以看出, Client 与 Server 在成功地建立了3 个连接后, 就无法再创建其余的连接了, 因为服务器的队已经满了.
//
//(2)在Server中构造一个跟(1)相同的 ServerSocket对象, Server程序不睡眠, 在一个 while 循环中不断执行 serverSocket.accept()方法, 该方法从队列中取出连接请求, 使得队列能及时腾出空位, 以容纳新的连接请求. Client 程序的打印结果如下:
//第 1 次连接成功
//第 2 次连接成功
//第 3 次连接成功
//………..
//第 100 次连接成功
//从以上打印结果可以看出, 此时 Client 能顺利与 Server 建立 100 次连接.(每次while的循环要够快才行, 如果太慢, 从队列取连接请求的速度比放连接请求的速度慢的话, 不一定都能成功连接)
