package com.iot.raspi;

import com.iot.server.WebServer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SocketController {
	static GpioPinDigitalOutput devicepin;
	static PinState devicepinstate;
	static Pin socketPin;
	static boolean onFlag, offFlag;
	
	public static boolean turnDeviceOn(int socket) {
		socketPin = WebServer.mapSocketsToGPIO.get(socket);
		devicepin = WebServer.gpio.provisionDigitalOutputPin(socketPin, "led pin");
		devicepin.setState(PinState.HIGH);
		devicepinstate = devicepin.getState();
		onFlag = devicepinstate.isHigh();
		WebServer.gpio.unprovisionPin(devicepin);
		
		return onFlag;
	}
	
	public static boolean turnDeviceOff(int socket) {
		socketPin = WebServer.mapSocketsToGPIO.get(socket);
		devicepin = WebServer.gpio.provisionDigitalOutputPin(socketPin, "led pin");
		devicepin.setState(PinState.LOW);
		devicepinstate = devicepin.getState();
		offFlag = devicepinstate.isHigh();
		WebServer.gpio.unprovisionPin(devicepin);
		
		return offFlag;
	}
}