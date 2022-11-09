package com.briup.smart.env.main;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.ConfigurationImpl;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.server.ServerImpl;

/**
 * 服务端入口类
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        ConfigurationImpl instance=ConfigurationImpl.getINstance();
        Server server=instance.getServer();
        server.reciver();

//        server.shutdown();
    }
}
