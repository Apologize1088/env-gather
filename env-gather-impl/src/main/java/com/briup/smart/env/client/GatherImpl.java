package com.briup.smart.env.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/*
自动生成引用：Ctrl+alt+v或者.var
智能键：alt+enter
 */
public class GatherImpl implements Gather  ,ConfigurationAware , PropertiesAware {

    private Log log;
    private String filePath;
    @Override
    public Collection<Environment> gather() {
        log.debug("采集模块：开始采集数据");
        //String filePath = "C:\\Users\\LX\\Desktop\\测试备份.txt";
        //E:\\2022\\pdf8 项目\\data-file
        ArrayList<Environment> list = new ArrayList<>();
        try {
            //1.用io流读取文件中的数据，用缓冲输入流BufferedReader
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            //br = new BufferedReader(new FileReader(filePath));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                //字符串分割操作，按|进行分割
                String[] arr = str.split("[|]");
                Environment environment = new Environment();
                //环境种类名称
                //private String name;
                //发送端id
                environment.setSrcId(arr[0]);
                //树莓派系统id]
                environment.setDesId(arr[1]);

                //实验箱区域模块id(1-8)
                environment.setDevId(arr[2]);
                //模块上传感器地址
                environment.setSersorAddress(arr[3]);
                //传感器个数
                environment.setCount(Integer.parseInt(arr[4]));
                //发送指令标号 3表示接收数据 16表示发送命令
                environment.setCmd(arr[5]);
                //状态 默认1表示成功
                environment.setStatus(Integer.parseInt(arr[7]));
                //环境值
                //private float data;
                //采集时间
                environment.setGather_date(
                        new Timestamp(Long.parseLong(arr[8]))
                );
                switch (arr[3]) {
                    case "16":
                        //如果当前是温度和湿度的数据，则前两个字节是温度，中间两个字节是湿度，剩余字节不用管
//                        16，表示温度和湿度数据
                        //5d606f7802
                        //一个字节可以由2个十六进制表示,5d60
                        //表示温度
                        environment.setName("温度");
                        String temperature = arr[6].substring(0, 4);
                        //16进制转10进制
                        int t = Integer.parseInt(temperature, 16);
                        environment.setData((t * (0.00268127F)) - 46.85F);
                        list.add(environment);

                        //表示湿度
                        Environment environment1 = new Environment();
                        //发送端id
                        environment1.setSrcId(arr[0]);
                        //树莓派系统id]
                        environment1.setDesId(arr[1]);

                        //实验箱区域模块id(1-8)
                        environment1.setDevId(arr[2]);
                        //模块上传感器地址
                        environment1.setSersorAddress(arr[3]);
                        //传感器个数
                        environment1.setCount(Integer.parseInt(arr[4]));
                        //发送指令标号 3表示接收数据 16表示发送命令
                        environment1.setCmd(arr[5]);
                        //状态 默认1表示成功
                        environment1.setStatus(Integer.parseInt(arr[7]));
                        environment.setGather_date(
                                new Timestamp(Long.parseLong(arr[8]))
                        );
                        environment1.setName("湿度");
                        String temperature1 = arr[6].substring(4, 8);
                        //16进制转10进制
                        int t1 = Integer.parseInt(temperature1, 16);
                        environment1.setData((t * 0.00190735F) - 6);

                        environment1.setGather_date(new Timestamp(Long.parseLong(arr[8])));
                        list.add(environment1);
                        break;

//                  256，表示光照强度数据,前两个字节就是数据值，剩余字节不用管
                    case "256":
                        environment.setName("光照强度");
                        environment.setData(Integer.parseInt(arr[6].substring(0, 4), 16));
                        list.add(environment);
                        break;

//                  1280，表示二氧化碳数据,前两个字节就是数据值，剩余字节不用管
                    case "1280":
                        environment.setName("二氧化碳浓度");
                        environment.setData(Integer.parseInt(arr[6].substring(0, 4), 16));
                        list.add(environment);
                        break;
                    default:
                        log.debug("数据格式错误:" + str);
                        break;
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log=configuration.getLogger();
    }

    @Override
    public void init(Properties properties) throws Exception {
        filePath=properties.getProperty("gather-file-path");
    }
}