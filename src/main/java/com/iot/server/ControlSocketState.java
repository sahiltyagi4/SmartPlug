package com.iot.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.iot.databean.SocketState;
import com.iot.raspi.SocketController;

public class ControlSocketState extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String setToState;
	boolean deviceState=false;
	int socket;
	SocketState socketState;
	JsonObject jsonObject;
	PrintWriter writer;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		setToState = req.getParameter("switch");
		socket = Integer.parseInt(req.getParameter("socket"));
		
		if(setToState.equalsIgnoreCase("on")) {
			deviceState = SocketController.turnDeviceOn(socket);
		} else if(setToState.equalsIgnoreCase("off")) {
			deviceState = SocketController.turnDeviceOff(socket);
		}
		
		socketState = new SocketState();
		socketState.setCurrentSocketState(deviceState);
		WebServer.socketStates.put(socket, socketState);
		
		jsonObject = new JsonObject();
		jsonObject.addProperty("isSocketOn", deviceState);
		
		writer = resp.getWriter();
		writer.println(jsonObject.toString());
	}

}
