package com.briup.smart.env.main;

import com.briup.smart.env.ConfigurationImpl;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.ClientImpl;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.entity.Environment;

import java.util.Collection;

/**
 * 客户端入口类
 */
public class ClientMain {
    public static void main(String[] args) throws Exception {
        ConfigurationImpl instance=ConfigurationImpl.getINstance();

        Gather gather= instance.getGather();

        Collection<Environment> collection=gather.gather();

        Client client=  instance.getClient();
        client.send(collection);

    }
}
