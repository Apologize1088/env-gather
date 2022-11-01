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

        try {
            //采集数据
            GatherImpl gather = new GatherImpl();
            Collection<Environment> gather1 = gather.gather();
            //发送数据
            new ClientImpl().send(gather1);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}