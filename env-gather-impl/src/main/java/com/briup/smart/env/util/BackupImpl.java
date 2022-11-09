package com.briup.smart.env.util;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.support.ConfigurationAware;

import java.io.*;

public class BackupImpl implements Backup, ConfigurationAware {
    private  Log log;
    @Override
    public Object load(String fileName, boolean del) throws Exception {
        File file=new File(fileName);
        if (!file.exists()){
            log.info("备份模块，需要读取的文件不存在"+fileName);
            return  null;
        }
        Object object=null;
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName)))
        {
            object=ois.readObject();
            log.info("备份模块，成功读取备份文件");
        }catch (Exception e){
            e.printStackTrace();
        }if (del){
            boolean delete=file.delete();
            log.info("备份模块：文件"+(delete?"删除成功":"删除失败"));
        }
        return object;
    }

    /**
     * 保存数据到备份文件
     * @param fileName 备份文件的绝对路径
     * @param obj 想要保存是数据
     * @param append 保存时是否追加
     * @throws Exception
     */
    @Override
    public void store(String fileName, Object obj, boolean append) throws Exception {
        try(
        ObjectOutputStream oos=
                new ObjectOutputStream(new FileOutputStream(fileName,append))
        ){
            oos.writeObject(obj);
            log.info("备份模块：数据已保存到备份文件中"+fileName);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {

    }
}
