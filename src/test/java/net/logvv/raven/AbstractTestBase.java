package net.logvv.raven;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public abstract class AbstractTestBase
{
    /**
     *  使用说明：
     *  1、其他测试类需继承该类
     *  2、spring 数据库测试会自动回滚数据，消除测试对数据库的污染
     *  3、若需要在数据库中查看测试修改的数据，需将defaultRollback 指定为false
     *  
     */
    
    
}