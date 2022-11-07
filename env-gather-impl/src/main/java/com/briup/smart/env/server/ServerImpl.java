package com.briup.smart.env.server;

import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

public class ServerImpl implements  Server{
    private  boolean stop;
    private  ServerSocket serverSocket;
    private Log log = new LogImpl();
    @Override
    public void receive() throws Exception {
        new Thread(()->{
            try {
                //接收客户端发来的数据，要一直接收
                serverSocket = new ServerSocket(9999);
                log.debug("服务器启动，等待连接");
                while (! stop) {
                    Socket socket = serverSocket.accept();
                    log.debug("连接成功");
                    new Thread(() -> {
                        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
                            log.debug("服务端准备接收数据");
                            Object object = objectInputStream.readObject();

                            Collection<Environment> collection = (Collection<Environment>) object;
                            log.debug("服务端接收数据完毕，共" + collection.size() + "条");
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }catch ( Exception e){
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void shutdown() throws Exception {
        //关闭服务端
        stop=true;
        if (serverSocket !=null)
            serverSocket.close();
        log.debug("shutdown执行完毕");

    }
}