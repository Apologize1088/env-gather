package com.briup.smart.env.cilent;

import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GatherImpl implements Gather {
    @Override
    public Collection<Environment> gather() throws Exception {
        List<Environment> list = new ArrayList<>();
        File file = new File("");
        BufferedReader br = null;

        br = new BufferedReader(new FileReader(file));
        String countent = "";
        while (!"".equals(countent = br.readLine())){
            String[] split = countent.split("|");
            String[] names = getName(split[3]);
            for (String name : names) {
                Environment environment = processingData(name, split);
                list.add(environment);
            }
        }
        return list;
    }

    /**
     * 处理数据
     * @param name1
     * @param split
     * @return
     */
    private Environment processingData(String name1,String[] split){
        String name = name1;
        String srcId = split[0];
        String desId = split[1];
        String devId = split[2];
        String sersorAddress = split[3];
        int count = Integer.getInteger(split[4]);
        String cmd = split[5];
        int status  = Integer.getInteger(split[7]);
        float data = getData(name,split[6]);
        Timestamp gatherData = getDate(split[8]);
        return new Environment(name,srcId,desId,devId,sersorAddress,count,cmd,status,data,gatherData);
    }

    private Timestamp getDate(String s) {
        String getDate = s;

    }


    private float getData(String sersorAddress,String data){

    }

    private String[] getName(String addressOfSensor){
        String[] name = null;
        if ("16".equals(addressOfSensor)){
            name = new String[]{"温度","湿度"};
        }
        if ("256".equals(addressOfSensor)){
            name = new String[]{"光照强度"};
        }
        if ("1280".equals(addressOfSensor)){
            name = new String[]{"光照强度"};
        }
        return name;
    }

}
