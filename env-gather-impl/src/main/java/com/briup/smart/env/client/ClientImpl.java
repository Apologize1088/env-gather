package com.briup.smart.env.client;

import com.briup.smart.env.entity.Environment;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

public class ClientImpl implements Client {

    private static String ip = "127.1.1.1";
    private static int port = 10001;

    @Override
    public void send(Collection<Environment> c) throws Exception {
        //使用网络编程技术 Socket 将数据发送到服务器上
        Socket socket = new Socket(ip,port);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(outputStream));

        //遍历集合并序列化发送
        for (Environment environment : c) {
            objectOutputStream.writeObject(environment);
        }

        while (true){

        }
        //关闭流
//        socket.close();
//        outputStream.close();

    }
}