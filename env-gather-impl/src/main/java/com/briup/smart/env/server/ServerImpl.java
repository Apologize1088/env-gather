package com.briup.smart.env.server;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ServerImpl implements Server , ConfigurationAware, PropertiesAware {
    boolean flag=true ;
   ServerSocket serverSocket;
   private Log log;
   private  DBStoreImpl dbStore;
   private  int port;
    @Override
    public void reciver() throws Exception {
        new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("服务端建立成功!");
                while (flag) {
                    Socket socket = serverSocket.accept();
                    System.out.println("成功与客户端建立连接!");
                    new Thread(() -> {
                        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
                            Object object = objectInputStream.readObject();
                            Collection<Environment> collection = (Collection<Environment>) object;
                            System.out.println("接收数据完毕共" + collection.size() + "条!");
                            //调用入库模块，保存到数据库
                            dbStore.saveDB(collection);
                            System.out.println("服务器接收数据完毕，共"+collection.size()+"条");



                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //接收客户端发来的数据，需一直接收
        //ServerSocket serverSocket = new ServerSocket(port);
        //System.out.println("服务端建立成功!");
        //Socket socket = null;
        //ObjectInputStream ois = null;
        /*ois = new ObjectInputStream(socket.getInputStream());
        Collection<Environment> list = (Collection<Environment>) ois.readObject();
        shutdown();
        System.out.println("服务器接收到的数据:");
        list.forEach(System.out::println);*/
        //serverSocket.close();
        //socket.close();
        //ois.close();
    }

    @Override
    public void shutdown() throws Exception {
        //关闭服务端
        this.flag = false;
        if (serverSocket != null) {
            serverSocket.close();
            System.out.println("shutdown执行完毕!");
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log=configuration.getLogger();
        dbStore=(DBStoreImpl) configuration.getDbStore();
    }

    @Override
    public void init(Properties properties) throws Exception {
        port=Integer.parseInt(properties.getProperty("server-port"));
    }
}
