package com.briup.smart.env;

import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationImpl implements Configuration {
    //使用Map集合保存对象
    Map<String,PropertiesAware>map = new HashMap<>();
    //读取和解析文件
    public ConfigurationImpl(){
        try {
            //SAXReader.read(xmlSource)() - 构建XML源的DOM4J文档
            Document doc = new SAXReader().read("env-gather-impl/src/main/resources");
            //Document.getRootElement() - 得到的XML的根元素。
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();
            for (Element element : elements){
                //获取全限定类名
                String className = element.attributeValue("class");

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Log getLogger() throws Exception {
        return null;
    }

    @Override
    public Server getServer() throws Exception {
        return null;
    }

    @Override
    public Client getClient() throws Exception {
        return null;
    }

    @Override
    public DBStore getDbStore() throws Exception {
        return null;
    }

    @Override
    public Gather getGather() throws Exception {
        return null;
    }

    @Override
    public Backup getBackup() throws Exception {
        return null;
    }
}
