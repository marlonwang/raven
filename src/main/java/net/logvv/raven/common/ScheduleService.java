package net.logvv.raven.common;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

@Service
public class ScheduleService
{
    
    /**
     * 
     * init(初始化：统计信息、用户信息)
     * 
     * @return void 返回类型
     * @author 1enny
     * @date 2015年10月20日 下午11:43:47
     * @version [1.0, 2015年10月20日]
     * @since version 1.0
     */
    @PostConstruct
    public void init()
    {
        System.out.println("Server init !");
    }
    
    /**
     * 
     * stop(容器被关闭的时候)
     * 
     * @return void 返回类型
     * @author 1enny
     * @date 2015年10月20日 下午11:45:31
     * @version [1.0, 2015年10月20日]
     * @since version 1.0
     */
    @PreDestroy
    public void stop()
    {
        System.out.println("Server stop !");
    }
    
 
}
