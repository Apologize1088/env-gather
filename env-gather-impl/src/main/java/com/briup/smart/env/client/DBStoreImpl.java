package com.briup.smart.env.client;

import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class DBStoreImpl implements DBStore {
    private Backup backup = new BackupImpl();
    private String backupFliePath = "C://Users//Administrator//Desktop//data-file-simple.txt";
    @Override
    public void saveDB(Collection<Environment> c)throws Exception{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = null;
        int saveCount = 0;//保存数据的数据量
        int testExpection = 0;//用于抛出异常
        try {
                 connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE",
                "briup", "briup");

            //设置手动提交事务、
            connection.setAutoCommit(false);
            int count=0;//用于控制批处理的执行
            int previousDay=-1;//用于表示前一条数据的天数
            PreparedStatement ps=null;
            //遍历集合
            for (Environment environment : c) {
                testExpection++;
                if (testExpection == 3){
                    throw new RuntimeException("测试");
                }
                //获取天数
                Timestamp gather_date = environment.getGather_date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(gather_date.getTime());
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                if (previousDay!=currentDay){
                    //如果前一条数据的天数与这一条数据的天数不一样 创建新的prepareStatement对象
                    //根据拿到的数据的日期去拼接表名
                    if (ps!=null) {
                        ps.executeBatch();
                        connection.commit();
                        saveCount +=count;
                        ps.close();
                        count = 0;
                    }
                    String sql="insert into e_detail_"+ currentDay +" values(?,?,?,?,?,?,?,?,?,?)";
                    ps = connection.prepareStatement(sql);
                }

                ps.setString(1, environment.getName());
                ps.setString(2, environment.getSrcId());
                ps.setString(3, environment.getDesId());
                ps.setString(4, environment.getDevId());
                ps.setString(5, environment.getSersorAddress());
                ps.setInt(6,environment.getCount());
                ps.setString(7, environment.getCmd());
                ps.setInt(8,environment.getStatus());
                ps.setFloat(9,environment.getData());
                ps.setDate(10,new Date(environment.getGather_date().getTime()));

                //为了提升效率  使用批处理
                ps.addBatch();
                count++;//循环一次
                if (count==2){
                    ps.executeBatch();
                    connection.commit();
                    saveCount +=count;
                    count=0;
                }
                //循环完成后  新的一天变成先前天
                previousDay=currentDay;
            }
            ps.executeBatch();
            connection.commit();
            saveCount += count;
            ps.close();
            System.out.println("入库模块写入数据完毕");
        }catch (Exception e){
            connection.rollback();//事务回滚
            //如果发生异常，储存未保存的数据库的数据到备份文件中
            //connection接口缺失一些方法，所以需要转换成List
            ArrayList<Environment> list1 = (ArrayList<Environment>) c;
            List<Environment> list2 = list1.subList(saveCount, list1.size());
            //因为subList返回的集合没有事项Seriolizable接口，无法存储；
            ArrayList<Environment> list3 = new ArrayList<>();
            list3.addAll(list2);
            backup.store(backupFliePath,list3,Backup.STORE_OVERRIDE);
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.close();
            }
            System.out.println("入库模块：已保存的数据"+saveCount);
            System.out.println("入库模块：未保存的数据"+(c.size()-saveCount));
        }
    }

}
