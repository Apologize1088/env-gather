import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;
import java.util.ArrayList;

/**
 *备份模块的读取与测试
 * @author Administrator
 */
public class BackupImplTest {
    private String backupFliePath = "C://Users//Administrator//Desktop//data-file-simple.txt";
    @Test
    public void testStore() throws Exception {
        BackupImpl backup = new BackupImpl();
        backup.store(backupFliePath,1, Backup.STORE_OVERRIDE);

    }
    @Test
    public void testLoad() throws Exception {
        BackupImpl backup = new BackupImpl();
        Object object = backup.load(backupFliePath,Backup.LOAD_UNREMOVE);
        ArrayList<Environment> object1 = (ArrayList<Environment>) object;
        object1.forEach(System.out::println);
    }
    @Test
    public void test3(){
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.debug("Hello");
        rootLogger.error("");
    }
}
