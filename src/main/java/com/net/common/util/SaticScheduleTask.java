package com.net.common.util;

import java.text.ParseException;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SaticScheduleTask {
	
	
	 //每天0点同步更新消费记录
	 @Scheduled(cron = "0 0 0 */1 * ?")
	 private void configureTasks(){
		 
	 }

}
