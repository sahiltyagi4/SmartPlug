package com.iot.raspi;

import com.iot.server.WebServer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class PIRMotionSensor {
	static GpioPinDigitalOutput sensorOutput;
	static PinState pinState;
	static boolean onFlag, offFlag;
	
	static GpioPinDigitalInput sensorInput;
	GpioController gpioMotionSensor;
	
	private PIRMotionSensor() {
		 gpioMotionSensor = GpioFactory.getInstance();
	}
	
	public static boolean setMotionSensorOn() {
		sensorOutput = WebServer.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "motion sensor");
		sensorOutput.setState(PinState.HIGH);
		pinState = sensorOutput.getState();
		onFlag = pinState.isHigh();
		WebServer.gpio.unprovisionPin(sensorOutput);
		return onFlag;
	}
	
	public static boolean setMotionSensorOff() {
		sensorOutput = WebServer.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "motion sensor");
		sensorOutput.setState(PinState.LOW);
		pinState = sensorOutput.getState();
		offFlag = pinState.isHigh();
		WebServer.gpio.unprovisionPin(sensorOutput);
		return offFlag;
	}
	
	private void isMotionSensorTriggered() {
		//GPIO 0 is pin number 11
		sensorInput = gpioMotionSensor.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
		sensorInput.addListener(new GpioPinListenerDigital() {
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				if(event.getState().isHigh()) {
					//make arrangements for sending push notifications on android
					System.out.println("motion sensor triggered...");
				}
			}
		});
	}
	
	public static void main(String[] args) {
		PIRMotionSensor sensorclass = new PIRMotionSensor();
		sensorclass.isMotionSensorTriggered();
	}
}
