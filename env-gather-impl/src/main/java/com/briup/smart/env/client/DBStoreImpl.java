package com.briup.smart.env.client;

import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.DBStore;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;

public class DBStoreImpl implements DBStore {
    @Override
    public void saveDB(Collection<Environment> c)throws Exception{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE",
                "briup",
                "briup");
        ){
            int count=0;//用于控制批处理的执行
            int previousDay=-1;//用于表示前一条数据的天数
            PreparedStatement ps=null;
            //遍历集合
            for (Environment environment : c) {
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
                if (count==500){
                    ps.executeBatch();
                    count=0;
                }
                //循环完成后  新的一天变成先前天
                previousDay=currentDay;
            }
            ps.executeBatch();
            ps.close();
            System.out.println("入库模块写入数据完毕");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
