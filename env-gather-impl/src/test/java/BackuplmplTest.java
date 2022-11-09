import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.security.PublicKey;
import java.util.ArrayList;

public class BackuplmplTest<Public> {
    private String backupFilePath="E:\\\\briup 企业\\\\Emotional\\\\env-gather\\\\备份文件.txt";
    @Test
    public  void  testStore() throws Exception {
        BackupImpl backup=new BackupImpl();
        backup.store(backupFilePath,1, Backup.STORE_OVERRIDE);
    }
    @Test
    public void testLoad() throws Exception {
        BackupImpl backup=new BackupImpl();
        Object object=backup.load(backupFilePath,Backup.LOAD_UNREMOVE);
        ArrayList<Environment> object1=(ArrayList<Environment>) object;
        object1.forEach(System.out::println);
    }
    @Test
    public void  test3(){
        Logger rootLogger=Logger.getRootLogger();
        rootLogger.debug("hello");
        rootLogger.error("hhh");
    }
}
