package com.briup.smart.env.main;

import com.briup.smart.env.client.ClientImpl;

import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.entity.Environment;

import java.util.Collection;

/**
 * 客户端入口类
 */
public class ClientMain {
    public static void main(String[] args) {
        GatherImpl gather = new GatherImpl();
        Collection<Environment> collection=gather.gather();

        ClientImpl client=new ClientImpl();
        client.send(collection);

    }
}
