import com.lagou.edu.dao.AccountDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyIoCTest {

    @Test
    public void testIoCInitiation() {

        // xml模式 SE应用
        // 通过读取classpath下的xml文件来启动容器(推荐)
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        // 不推荐使用,因为代码迁移时会受到限制
        //ApplicationContext applicationContext1 = new FileSystemXmlApplicationContext("文件系统的绝对路径");

        AccountDao accountDao = (AccountDao) applicationContext.getBean("accountDao");
        System.out.println(accountDao);
    }

}
