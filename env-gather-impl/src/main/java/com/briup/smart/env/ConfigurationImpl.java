package com.briup.smart.env;

import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;
import org.apache.log4j.net.SyslogAppender;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class ConfigurationImpl  implements Configuration{
    private static   ConfigurationImpl configuration=new ConfigurationImpl();
    private static String configFilePath=
            "E:\\briup 企业\\Emotional\\env-gather\\env-gather-impl\\src\\main\\resources\\conf.xml";
    private  static HashMap<String,Object> map=new HashMap<>();
    //原来储存conf。xml里的各种配置信息，例如IP，端口号等
    private  static Properties properties=new Properties();
    //私有化构造器，防止外界通过new关键字来创建对象
    private  ConfigurationImpl(){

    }
    //获取该类对象
    public  static ConfigurationImpl getINstance(){
        return configuration;
    }



    static {
        SAXReader  saxReader=new SAXReader();
        try {
            Document document=saxReader.read(new File(configFilePath));
            //获取xml文档里的根元素
            Element rootElement=document.getRootElement();
             List<Element> elements=rootElement.elements();
             for (Element element:elements){
                String name= element.getName();
                //获取String类型的全限定类名
                String aClss= element.attribute("class").getText();
               Class<?> aClass1= Class.forName(aClss);
               Object object=aClass1.newInstance();
               map.put(name,object);
              //获取根元素的子元素的子元素，
               List<Element> elements1=element.elements();
               for (Element element1:elements1){
                   String name1=element1.getName();
                   String text=element1.getText();
                   //将信息添加到properties中
                   properties.setProperty(name1,text);
               }
             }
             //遍历map中的值（各个模块的对象)，调用setConfiguration
            //init方法完成模块的初始化赋值操作
            for (Object value : map.values()) {
                if (value instanceof  ConfigurationAware){
                    //为了调用到setConfiguration方法，需要强转
                    ConfigurationAware configurationAware=(ConfigurationAware) value;
                    configurationAware.setConfiguration(configuration);
                }

                if (value instanceof PropertiesAware){
                    PropertiesAware propertiesAware=(PropertiesAware)value;
                    propertiesAware.init(properties);
                }
            }
        } catch (DocumentException | ClassNotFoundException |IllegalAccessException |InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Log getLogger() throws Exception {
        return(Log) map.get("logger");
    }

    @Override
    public Server getServer() throws Exception {
        return (Server)map.get("server");
    }

    @Override
    public Client getClient() throws Exception {
        return (Client) map.get("client");
    }

    @Override
    public DBStore getDbStore() throws Exception {
        return (DBStore ) map.get("dbStore");
    }

    @Override
    public Gather getGather() throws Exception {
        return (Gather) map.get("gather");
    }

    @Override
    public Backup getBackup() throws Exception {
        return (Backup) map.get("backup");
    }
}
