package com.net.common.config;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
	@Autowired
	private JobFactory jobFactory;
	
	@Autowired
	private AutowireCapableBeanFactory  capableBeanFactory;
	
	/**
	 * 当触发器触发时，与之关联的任务被Scheduler中配置的JobFactory实例化，也就是每触发一次，就会创建一个任务的实例化对象
	 * (如果缺省)则调用Job类的newInstance方法生成一个实例
	 * (这里选择自定义)并将创建的Job实例化交给IoC管理
	 * @return
	 */
	@Bean
	public JobFactory jobFactory() {
		return new AdaptableJobFactory() {
			@Override
			protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
				Object jobInstance = super.createJobInstance(bundle);
				capableBeanFactory.autowireBean(jobInstance);
				return jobInstance;
			}
		};
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(jobFactory);
         String path = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"template";
      //   System.out.println(path);
		//延迟启动
		schedulerFactoryBean.setStartupDelay(0);
		schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
		return schedulerFactoryBean;
	}
	
	@Bean
	public Scheduler scheduler() {
		return schedulerFactoryBean().getScheduler();
	}
}
