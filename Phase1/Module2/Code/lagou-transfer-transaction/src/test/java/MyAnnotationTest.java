import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.factory.BeanFactory;
import com.lagou.edu.service.TransferService;
import com.lagou.edu.utils.MyUtil;
import org.junit.Test;

import java.lang.reflect.Field;

public class MyAnnotationTest {

    @Test
    public void testMyServiceMyComponentAnnotation() {
        TransferService service = (TransferService) BeanFactory.getBean("myTransferService");
        System.out.println("My service: " + service);

        AccountDao ad = (AccountDao) BeanFactory.getBean("myDao");
        System.out.println("My dao: " + ad);
    }

    @Test
    public void testMyAutowiredAnnotation() throws Exception {
        TransferService service = (TransferService) BeanFactory.getBean("myTransferService");
        service.transfer("34","999", 200);
    }
}
