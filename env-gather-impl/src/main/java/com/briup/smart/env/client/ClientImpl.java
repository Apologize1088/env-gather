package com.briup.smart.env.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ClientImpl implements Client, ConfigurationAware , PropertiesAware {
    private Log log;
    private  String host;
    private  int port;
    @Override
    public void send(Collection<Environment> c) {
        if(c==null||c.size()==0){
            log.debug("客户端网络模块：接收的数据有误");
            return;
        }
        //使用网络编程技术 Socket 将数据发送到服务端
        try {
            Socket socket=new Socket(host,port);
            ObjectOutputStream objectOutputStream=
                    new ObjectOutputStream(socket.getOutputStream());
            log.debug("客户端网络模块：客户端连接成功");
            log.debug("客户端网络模块：客户端准备发送数据");
            objectOutputStream.writeObject(c);
            log.debug("数据接收成功，共"+c.size()+"条");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log=configuration.getLogger();
    }

    @Override
    public void init(Properties properties) throws Exception {
        host=properties.getProperty("client-host");
        port=Integer.parseInt(properties.getProperty("client-port"));

    }
}
