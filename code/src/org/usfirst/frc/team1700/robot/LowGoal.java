package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;

public class LowGoal {
	Victor highRampMotor;
	Victor lowRampMotor;
	DigitalInput highTopSensor;
	DigitalInput lowTopSensor;
	DigitalInput bottomSensor;
	
	public LowGoal() {
		highRampMotor = new Victor(Constants.PWM.FIRST_DUMPER_MOTOR.getPort());
		lowRampMotor = new Victor(Constants.PWM.SECOND_DUMPER_MOTOR.getPort());
		highTopSensor = new DigitalInput(Constants.DigitalIO.HIGH_TOP_RAMP_ID.getPort());
		lowTopSensor = new DigitalInput(Constants.DigitalIO.LOW_TOP_RAMP_ID.getPort());
		bottomSensor = new DigitalInput(Constants.DigitalIO.BOTTOM_SENSOR_ID.getPort());
	}
	
	
	public void moveUp() {
		if(!highTopSensor.get()) {
			highRampMotor.set(0.6);
		} else {
			highRampMotor.set(0);
		}
		if(!lowTopSensor.get()) {
			lowRampMotor.set(0.6);
		} else {
			lowRampMotor.set(0);
		}
	}
	
		
	public void moveDown() {
		if (!bottomSensor.get()) {
			highRampMotor.set(0.7);
			lowRampMotor.set(0.4);
		} else {
			highRampMotor.set(0);
			lowRampMotor.set(0);
		}
	}
}
