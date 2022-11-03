package com.briup.smart.env.main;

import com.briup.smart.env.server.ServerImpl;

/**
 * 服务端入口类
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        ServerImpl server=new ServerImpl();
        server.receive();
        //Thread.sleep(5000);
        //server.shutdown();
    }
}
