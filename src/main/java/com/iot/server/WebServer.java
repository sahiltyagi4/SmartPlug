package com.iot.server;

import java.util.HashMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.iot.databean.SocketState;
import com.iot.misc.RaspberryPiUtils;
import com.iot.server.FetchCurrentSocketState;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;

public class WebServer {
	public static HashMap<Integer, SocketState> socketStates = new HashMap<Integer, SocketState>();
	public static HashMap<Integer, Pin> mapSocketsToGPIO;
	public final static GpioController gpio = GpioFactory.getInstance();
	
	public static void main(String[] args) {
		Server webserver = new Server(6969);
		ServletHandler handler = new ServletHandler();
		webserver.setHandler(handler);
		
		RaspberryPiUtils raspberryPiUtils = new RaspberryPiUtils();
		mapSocketsToGPIO = raspberryPiUtils.mapSocketsToGPIO();
		raspberryPiUtils.switchSocketsOff();
		
		//http://localhost:6969/controlsocket?switch=on&socket=1
		handler.addServletWithMapping(ControlSocketState.class, "/controlsocket");
		//http://localhost:6969/getcurrentsocketstate?socket=1
		handler.addServletWithMapping(FetchCurrentSocketState.class, "/getcurrentsocketstate");
		//http://localhost:6969/socketstatescheduler?startTimestamp=XXX&endTimestamp=XXX&socket=1
		handler.addServletWithMapping(SocketStateScheduler.class, "/socketstatescheduler");
		//http://localhost:6969/switchOnMotionSensor?switch=on
		handler.addServletWithMapping(MotionSensorSwitch.class, "/motionSensorState");
		
		try {
			webserver.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			webserver.join();
		} catch(InterruptedException i) {
			i.printStackTrace();
		}
	}
}
