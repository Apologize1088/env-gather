package com.briup.smart.env.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

public class ClientImpl implements Client, ConfigurationAware {
    private Log log;
    @Override
    public void send(Collection<Environment> c) {
        //使用网络编程技术 Socket  将数据发送到服务端
        if (c==null||c.size()==0){
            log.debug("接收数据有误");
            return;
        }
        try (
                Socket socket = new Socket("127.0.0.1", 9999);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ){
            log.debug("客户端连接成功");
            log.debug("客户端准备发送数据");
            objectOutputStream.writeObject(c);
            log.debug("数据发送成功，共"+c.size()+"条");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log = configuration.getLogger();
    }
}