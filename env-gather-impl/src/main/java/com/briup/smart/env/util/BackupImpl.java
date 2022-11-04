package com.briup.smart.env.util;

import java.io.*;

public class BackupImpl implements Backup {

    @Override
    public Object load(String fileName, boolean del) throws Exception {
        File file = new File(fileName);
        if (!file.exists()){
            System.out.println("想要读取的备份文件不存在"+fileName);
        }
        Object object = null;
        try(ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(fileName))){
            object = ois.readObject();
            System.out.println("备份模块：成功读取备份文件");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (del){
            boolean delete = file.delete();
            System.out.println("备份模块：文件"
                    +(delete?"删除成功":"删除失败"));
        }
        return object;
    }

    @Override
    public void store(String fileName, Object obj, boolean append) throws Exception {
        try(
                ObjectOutputStream oos =
                        new ObjectOutputStream(new FileOutputStream(fileName,append));
                ){
            oos.writeObject(obj);
            System.out.println("备份模块：数据已保存到备份文件中"+fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}