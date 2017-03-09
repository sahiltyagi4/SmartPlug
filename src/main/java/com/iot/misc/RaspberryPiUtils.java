package com.iot.misc;

import java.util.HashMap;

import com.iot.databean.SocketState;
import com.iot.server.WebServer;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class RaspberryPiUtils {
	HashMap<Integer, Pin> socketsGPIO = new HashMap<Integer, Pin>();
	SocketState socketState;
	
	public HashMap<Integer, Pin> mapSocketsToGPIO() {
		socketsGPIO.put(0, RaspiPin.GPIO_00);	//pin 11 on raspberry pi reserved for motion sensor (+3.3V)
		socketsGPIO.put(1, RaspiPin.GPIO_01);	//pin 12 on raspberry pi reserved for motion sensor (O/P)
		socketsGPIO.put(2, RaspiPin.GPIO_02);	//pin 13 on raspberry pi
		socketsGPIO.put(3, RaspiPin.GPIO_03);	//pin 15 on raspberry pi
		socketsGPIO.put(4, RaspiPin.GPIO_04);	//pin 16 on raspberry pi
		socketsGPIO.put(5, RaspiPin.GPIO_05);	//pin 18 on raspberry pi
		socketsGPIO.put(6, RaspiPin.GPIO_06);	//pin 22 on raspberry pi
		socketsGPIO.put(7, RaspiPin.GPIO_07);	//pin 7 on raspberry pi
		
		return socketsGPIO;
	}
	
	public void switchSocketsOff() {
		socketState = new SocketState();
		for(int i=0; i< WebServer.mapSocketsToGPIO.size(); i++) {
			socketState.setCurrentSocketState(false);
			WebServer.socketStates.put(i, socketState);
		}
	}
}
