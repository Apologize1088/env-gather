package com.briup.smart.env;

import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;
import org.junit.Test;

import java.util.Collection;


public class ConfigurationImplTest {

    @Test
    public void getINstance() throws Exception{
        ConfigurationImpl instance=ConfigurationImpl.getINstance();
        Gather gather=instance.getGather();
        Collection<Environment> collection=gather.gather();
        System.out.println(collection);

    }
}