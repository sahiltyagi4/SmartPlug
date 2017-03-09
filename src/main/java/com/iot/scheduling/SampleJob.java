package com.iot.scheduling;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SampleJob implements Job {
	static Scheduler scheduler;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("successfully started the trigger on job...");
		try {
			scheduler.shutdown();
		} catch(SchedulerException s) {
			s.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws SchedulerException {
		JobKey jobKey = new JobKey("sample1", "group1");
		JobDetail jobDetail = JobBuilder.newJob(SampleJob.class).withIdentity(jobKey).build();
		long currts = System.currentTimeMillis();
		System.out.println(currts);
		Date date1 = new Date(currts);
		System.out.println("date1: "+ date1);
		//Date startDate = new Date(1488339808308L + 30000);
		Date startDate = new Date(currts);
		System.out.println("current date is: "+ startDate);
		int year = startDate.getYear() + 1900;
		int month = startDate.getMonth() + 1;
		int dayOfMonth = startDate.getDate();
		int hour = startDate.getHours();
		int minutes = startDate.getMinutes();
		int seconds = startDate.getSeconds();
		
		String cron = seconds+" "+minutes+" "+hour+" "+dayOfMonth+" "+month+" ? "+year;
		System.out.println(cron);
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("sample1", "group1")
						.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		
		
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);
	}

}
