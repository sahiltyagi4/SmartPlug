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

public class StopTimeScheduler implements Job {
	Scheduler scheduler;
	JobDetail jobDetail;
	static int socket;
	Trigger stopTrigger;
	Date stopDate;
	Pin socketPin;
	GpioPinDigitalOutput ledState;
	int year, month, dayOfMonth, hour, minutes, seconds;
	JobKey jobKey = new JobKey("stopScheduler", "stopGroup");
	SocketState socketState;
	String cron;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		socketPin = WebServer.mapSocketsToGPIO.get(socket);
		ledState = WebServer.gpio.provisionDigitalOutputPin(socketPin, "led pin");
		ledState.setState(PinState.LOW);
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
	public void stopSocketScheduler(long stopTimestamp, int socket) throws SchedulerException {
		this.socket = socket;
		jobDetail = JobBuilder.newJob(StopTimeScheduler.class).withIdentity(jobKey).build();
		
		stopDate = new Date(stopTimestamp);
		year = stopDate.getYear() + 1900;
		month = stopDate.getMonth() + 1;
		dayOfMonth = stopDate.getDate();
		hour = stopDate.getHours();
		minutes = stopDate.getMinutes();
		seconds = stopDate.getSeconds();
		
		cron = seconds + " " + minutes + " " + hour + " " + dayOfMonth + " " + month + " ? " + year;
		stopTrigger = TriggerBuilder.newTrigger().withIdentity("stopTrigger", "stopGroup")
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, stopTrigger);
	}
}
