package com.briup.smart.env.server;

import com.briup.smart.env.entity.Environment;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerImpl implements Server{
    //true 开启，false 关闭
    private static boolean sign = true;
    //端口号
    private static int port = 10001;

    //
    private static Socket socket = null;
    @Override
    public void receive() throws Exception {
        sign = true;
        //声明Socket

        String hostName = null;
        //接受客户端发来的数据 一直接受
        //建立连接获取socket并建立流
        ServerSocket serverSocket = new ServerSocket(port);
        while (sign) {
            socket= serverSocket.accept();
            hostName = socket.getInetAddress().getHostName();
            System.out.println(hostName + "以连接");
            //处理数据
            Thread t1 = new Thread(new MyThrea(socket));
            t1.start();
        }

    }

    @Override
    public void shutdown() throws Exception {
        //关闭客户端
        sign = false;
        socket = new Socket();
        socket.close();
    }

}

/**
 * 处理接受队象数据
 */
class MyThrea implements Runnable{
    Socket socket = null;

    public MyThrea(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            inputStream = socket.getInputStream();

            objectInputStream = new ObjectInputStream(new BufferedInputStream(inputStream));
            while (true) {
                //反序列化
                Object o = objectInputStream.readObject();
                Environment environment = null;
                if (o instanceof Environment){
                    environment = (Environment) o;
                }
                System.out.println(environment);
            }

        } catch (IOException e) {
            System.out.println("断开连接");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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