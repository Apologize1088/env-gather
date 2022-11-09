package com.briup.smart.env.server;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.util.*;


public class DBStoreImpl implements DBStore , ConfigurationAware, PropertiesAware {
    private Backup backup;
    private  String backupFilePath;
    private Log log;
    private  String url;
    private  String driver;
    private  String username;
    private  String password;
    //
    @Override
    public void saveDB(Collection<Environment> c) throws Exception{
        File file=new File(backupFilePath);
        if (file.exists()&&file.length()>0){
            Object obj=backup.load(backupFilePath,Backup.LOAD_REMOVE);
            if (obj instanceof  Collection){
                Collection<Environment> col=(Collection<Environment> ) obj;
                c.addAll(col);
                System.out.println("入库模块：备份文件中的数据量—"+col.size());
                System.out.println("入库模块：本次入库的总数据量—"+c.size());
            }
        }

        Class.forName(driver);
        Connection connection=null;
        int savedCount=0;//保存到数据库的数据量
        int testException =0;//用于抛出异常
        try{connection= DriverManager
                .getConnection(url,username,password);

            connection.setAutoCommit(false);//设置手动提交事务
            int count =0;
            int previousDay=-1;//表示前一条数据的天数
            PreparedStatement ps=null;

            //遍历集合
           // String sql="insert into e_detail_"+currentDay+
           //         "vales(?,?,?,?,?,?,?,?,?,?)";
            //ps = connection.prepareStatement(sql);
            for (Environment environment :c){
               /** testException++;

               if (testException==3){
                    throw  new RuntimeException("测试");
                }*/

                Timestamp gather_date=environment.getGather_date();
                Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(gather_date.getTime());
                int currentDay=calendar.get(Calendar.DAY_OF_MONTH);
                if(previousDay!=currentDay) {
                    //根据拿到的
                    if (ps != null) {
                        ps.executeBatch();
                        connection.commit();
                        savedCount+=count;
                        ps.close();
                        count = 0;
                    }

                    /*如果前一天数据的天数与这一天数据的天数不一样，创建
                      新的prepareStatement对象
                    */
                    //根据拿到的数据的日期去拼接表名
                    String sql = "insert into e_detail_" + currentDay +
                            " values(?,?,?,?,?,?,?,?,?,?)";
                    ps = connection.prepareStatement(sql);
                }




                ps.setString(1,environment.getName());
                ps.setString(2,environment.getSrcId());
                ps.setString(3,environment.getDesId());
                ps.setString(4,environment.getDevId());
                ps.setString(5,environment.getSersorAddress());
                ps.setInt(6,environment.getCount());
                ps.setString(7,environment.getCmd());
                ps.setInt(8,environment.getStatus());
                ps.setFloat(9,environment.getData());
                ps.setDate(10,new Date(environment.getGather_date().getTime()));
                //为了提升效率，使用批处理
                ps.addBatch();
                count++;

                if (count==2){
                    ps.executeBatch();
                    connection.commit();
                    savedCount+=count;
                    //ps.clearBatch();
                    count=0;
                }
                previousDay=currentDay;
            }
            ps.executeBatch();
            connection.commit();
            savedCount+=count;
            ps.close();
            System.out.println("入库模块写入数据完毕");


        }catch (Exception e){
            connection.rollback();//事务回滚
            //发生异常，存储未保存数据到备份文件中
            ArrayList<Environment> list1=(ArrayList<Environment>) c;
            List<Environment>list2=list1.subList(savedCount,list1.size());
            //subList方法返回的集合没有实现Serializable接口，无法储存
            ArrayList<Environment> list3=new ArrayList<>();
            list3.addAll(list2);
            backup.store(backupFilePath,list3,Backup.STORE_OVERRIDE);
            e.printStackTrace();
        }finally {
            if (connection!=null){
                connection.close();
            }
            System.out.println("入库模块：已保存的数据"+savedCount);
            System.out.println("入库模块：未保存的数据"+(c.size()-savedCount));
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log=configuration.getLogger();
        backup=configuration.getBackup();
    }

    @Override
    public void init(Properties properties) throws Exception {
        backupFilePath=properties.getProperty("backup-file-path");
        driver=properties.getProperty("driver");
        url=properties.getProperty("url");
        username=properties.getProperty("username");
        password=properties.getProperty("password");

    }
}