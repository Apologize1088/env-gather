package com.briup.smart.env.test;

import com.briup.smart.env.server.ServerImpl;
import org.junit.jupiter.api.Test;


public class ServerImplTest {
    @Test
    public void shoutdownTest(){
        try {
            new ServerImpl().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}