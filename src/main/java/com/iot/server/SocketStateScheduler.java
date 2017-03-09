package com.iot.server;

import java.time.DateTimeException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;

import com.iot.scheduling.StartTimeScheduler;
import com.iot.scheduling.StopTimeScheduler;

public class SocketStateScheduler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long startTimestamp = 0L, endTimestamp = 0L;
	int socket;
	StartTimeScheduler startTimeScheduler = new StartTimeScheduler();
	StopTimeScheduler stopTimeScheduler = new StopTimeScheduler();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		startTimestamp = Long.parseLong(req.getParameter("startTimestamp"));
		endTimestamp = Long.parseLong(req.getParameter("endTimestamp"));
		socket = Integer.parseInt(req.getParameter("socket"));
		 
		if(startTimestamp < endTimestamp) {
			try {
				startTimeScheduler.startSocketScheduler(startTimestamp, socket);
			} catch(SchedulerException s) {
				s.printStackTrace();
			}
			try {
				stopTimeScheduler.stopSocketScheduler(endTimestamp, socket);
			} catch(SchedulerException s) {
				s.printStackTrace();
			}
		} else {
			throw new DateTimeException("start time CANNOT BE GREATER THAN end time");
		}
	}
}
