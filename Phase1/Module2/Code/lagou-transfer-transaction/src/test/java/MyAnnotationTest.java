import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.factory.BeanFactory;
import com.lagou.edu.service.TransferService;
import org.junit.Test;

public class MyAnnotationTest {

    @Test
    public void testMyServiceMyComponentAnnotation() {
        TransferService service = (TransferService) BeanFactory.getBean("myTransferService");
        System.out.println("My service: " + service);

        AccountDao ad = (AccountDao) BeanFactory.getBean("myDao");
        System.out.println("My dao: " + ad);
    }
}
