import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.entity.Environment;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class GatherImplTest {
    @Test
    public void gatherTest(){
        try {
            Collection<Environment> gather = new GatherImpl().gather();
            System.out.println(gather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
