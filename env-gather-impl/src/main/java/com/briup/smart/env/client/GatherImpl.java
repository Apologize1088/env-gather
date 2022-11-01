package com.briup.smart.env.client;

import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;

import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GatherImpl implements Gather {
    @Override

    public Collection<Environment> gather() throws Exception {
        List<Environment> list = new ArrayList<>();
        File file = new File("C:\\Users\\Administrator\\Desktop\\data-file-simple");
        if (file==null){
            return null;
        }
        BufferedReader br = null;

        br = new BufferedReader(new FileReader(file));
        String countent = null;
        while ((countent = br.readLine())!=null){
            String[] split = countent.split("\\|");
            String[] names = getName(split[3]);
            for (String name : names) {
                Environment environment = processingData(name, split);
                list.add(environment);
            }
        }
        br.close();
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
        int count = Integer.valueOf(split[4]);
        String cmd = split[5];
        int status  = Integer.valueOf(split[7]);
        float data = getData(name,split[6]);
        Timestamp gatherData = getDate(split[8]);
        return new Environment(name,srcId,desId,devId,sersorAddress,count,cmd,status,data,gatherData);
    }

    /**
     * 获取时间
     * @param s
     * @return
     */
    private Timestamp getDate(String s) {
        long time = Long.valueOf(s);
        return new Timestamp(time);
    }


    /**
     * 环境数据处理
     * @param name
     * @param data
     * @return
     */
    private float getData(String name,String data){
        float concreteData = 0;
        char[] dataChars = data.toCharArray();
        if ("温度".equals(name)){
            String temperature = new String(dataChars, 0, 4);
            float i = Integer.parseInt(temperature,16);
            concreteData = (i * (0.00268127F))-46.85F;
        }
        if ("湿度".equals(name)){
            String humidity = new String(dataChars, dataChars.length/2-1, 4);
            float i = Integer.parseInt(humidity,16);
            concreteData = (i*0.00190735F)-6;

        }
        if ("光照强度".equals(name)){
            String illumination = new String(dataChars, 0, 4);
            concreteData = Integer.parseInt(illumination,16);

        }
        if ("二氧化碳".equals(name)){
            String carbonDioxide  = new String(dataChars, 0, 4);
            concreteData = Integer.parseInt(carbonDioxide,16);

        }
        return concreteData;
    }

    /**
     * 获取环境名称
     * @param addressOfSensor
     * @return
     */
    private String[] getName(String addressOfSensor){
        String[] name = null;
        if ("16".equals(addressOfSensor)){
            name = new String[]{"温度","湿度"};
        }
        if ("256".equals(addressOfSensor)){
            name = new String[]{"光照强度"};
        }
        if ("1280".equals(addressOfSensor)){
            name = new String[]{"二氧化碳"};
        }
        return name;
    }
}