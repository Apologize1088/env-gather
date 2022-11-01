package com.briup.smart.env.main;

import com.briup.smart.env.server.ServerImpl;

/**
 * 服务端入口类
 */
public class ServerMain {
    public static void main(String[] args) {
        //接受数据
        ServerImpl server = new ServerImpl();
        try {
            server.receive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}