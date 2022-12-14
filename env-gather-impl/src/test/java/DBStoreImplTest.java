import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.DBStoreImpl;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

public class DBStoreImplTest {
    @Test
    public void test() throws Exception {
        GatherImpl gather = new GatherImpl();
        Collection<Environment> collection = gather.gather();
        DBStoreImpl dbStore = new DBStoreImpl();
        dbStore.saveDB(collection);
    }
    @Test
    public void test3(){
        GatherImpl gather=new GatherImpl();
        Collection<Environment>collection=gather.gather();
        int[] arr=new int[31];
        for(Environment environment:collection){
            Timestamp gather_date=environment.getGather_date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(gather_date.getTime());
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            arr[day - 1]++;
        }
        for (int i=0;i<arr.length;i++){
            System.out.println("第"+(i+1)+"天="+arr[i]);
        }

    }
}
