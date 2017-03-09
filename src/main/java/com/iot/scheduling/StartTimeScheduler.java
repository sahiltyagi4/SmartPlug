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

import com.iot.databean.SocketState;
import com.iot.server.WebServer;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class StartTimeScheduler implements Job {
	Scheduler scheduler;
	JobDetail jobDetail;
	static int socket;
	Trigger startTrigger;
	Date startDate;
	Pin socketPin;
	GpioPinDigitalOutput ledState;
	int year, month, dayOfMonth, hour, minutes, seconds;
	JobKey jobKey = new JobKey("startScheduler", "startgroup");
	SocketState socketState;
	String cron;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		socketPin = WebServer.mapSocketsToGPIO.get(socket);
		ledState = WebServer.gpio.provisionDigitalOutputPin(socketPin, "led pin");
		ledState.setState(PinState.HIGH);
		socketState = new SocketState();
		socketState.setCurrentSocketState(ledState.isHigh());
		WebServer.socketStates.put(socket, socketState);
		WebServer.gpio.unprovisionPin(ledState);
		try {
			scheduler.shutdown();
		} catch(SchedulerException s) {
			s.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void startSocketScheduler(long startTimestamp, int socket) throws SchedulerException {
		this.socket = socket;
		jobDetail = JobBuilder.newJob(StartTimeScheduler.class).withIdentity(jobKey).build();
		
		startDate = new Date(startTimestamp);
		year = startDate.getYear() + 1900;
		month = startDate.getMonth() + 1;
		dayOfMonth = startDate.getDate();
		hour = startDate.getHours();
		minutes = startDate.getMinutes();
		seconds = startDate.getSeconds();
		
		cron = seconds + " " + minutes + " " + hour + " " + dayOfMonth + " " + month + " ? " + year;
		startTrigger = TriggerBuilder.newTrigger().withIdentity("startTrigger", "startgroup")
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, startTrigger);
	}
}