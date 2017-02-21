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
	DigitalInput highBottomSensor;
	DigitalInput lowBottomSensor;
	
	public LowGoal() {
		highRampMotor = new Victor(Constants.PWM.FIRST_DUMPER_MOTOR.getPort());
		lowRampMotor = new Victor(Constants.PWM.SECOND_DUMPER_MOTOR.getPort());
		highTopSensor = new DigitalInput(Constants.DigitalIO.HIGH_TOP_RAMP_ID.getPort());
		lowTopSensor = new DigitalInput(Constants.DigitalIO.LOW_TOP_RAMP_ID.getPort());
		highBottomSensor = new DigitalInput(Constants.DigitalIO.HIGH_BOTTOM_RAMP_ID.getPort());
		lowBottomSensor = new DigitalInput(Constants.DigitalIO.LOW_BOTTOM_RAMP_ID.getPort());
		
	}
	
	
	public void moveUp() {
		if(!highTopSensor.get()) {
			highRampMotor.set(-Constants.Values.LowGoal.MOVE_UP_SPEED);
		} else {
			highRampMotor.set(0);
		}
		if(!lowTopSensor.get()) {
			lowRampMotor.set(0.6);
		} else {
			lowRampMotor.set(0);
		}
		System.out.println("limit switch high: " + highTopSensor.get());
		System.out.println("limit switch low: " + lowTopSensor.get());
	}
	
		
	public void moveDown() {
		if (highBottomSensor.get()) {
			highRampMotor.set(0);
		} else {
			highRampMotor.set(0.3);
		}
		
		if (lowBottomSensor.get()) {
			lowRampMotor.set(0);
		} else {
			lowRampMotor.set(-0.3);
		}
		System.out.println("limit switch high: " + highTopSensor.get());
		System.out.println("limit switch low: " + lowTopSensor.get());
	}
}
