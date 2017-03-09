package com.iot.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.iot.databean.SocketState;
import com.iot.raspi.PIRMotionSensor;

public class MotionSensorSwitch extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String switchState;
	boolean flag;
	SocketState socketState;
	JsonObject jsonObject;
	PrintWriter writer;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		switchState = req.getParameter("switch");
		
		if(switchState.equalsIgnoreCase("on")) {
			flag = PIRMotionSensor.setMotionSensorOn();
		} else {
			flag = PIRMotionSensor.setMotionSensorOff();
		}
		
		socketState = new SocketState();
		socketState.setCurrentSocketState(flag);
		WebServer.socketStates.put(0, socketState);
		
		jsonObject = new JsonObject();
		jsonObject.addProperty("isMotionSensorOn", flag);
		
		writer = resp.getWriter();
		writer.println(jsonObject.toString());
	}
}
