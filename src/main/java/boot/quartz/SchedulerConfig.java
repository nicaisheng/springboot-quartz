package boot.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class SchedulerConfig {

    @Autowired
    private Environment environment;

    @Resource
    private BeanJobFactory beanJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSourceTest") DataSource dataSource) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        // 自定义Job Factory，用于Spring注入
        factory.setJobFactory(beanJobFactory);
        factory.setDataSource(dataSource);
        factory.setAutoStartup(true);
        factory.setSchedulerName("MyClusteredScheduler");
        //延迟10S启动
        factory.setStartupDelay(10);
        factory.setConfigLocation(new ClassPathResource("quartz.properties"));
        factory.start();
        return factory;
    }
}
