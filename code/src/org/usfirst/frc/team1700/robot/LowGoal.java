package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;

public class LowGoal {
	Victor firstRampMotor;
	Victor secondRampMotor;
	DigitalInput topSensor;
	DigitalInput bottomSensor;
	AnalogInput depthSensor;
	
	public LowGoal() {
		firstRampMotor = new Victor(Constants.PWM.FIRST_DUMPER_MOTOR.getPort());
		secondRampMotor = new Victor(Constants.PWM.SECOND_DUMPER_MOTOR.getPort());
		topSensor = new DigitalInput(Constants.DigitalIO.TOP_SENSOR_ID.getPort()); 
		depthSensor = new AnalogInput(Constants.AnalogInput.RAMP_RANGEFINDER.getPort());
		bottomSensor = new DigitalInput(Constants.DigitalIO.BOTTOM_SENSOR_ID.getPort());
	}
	
	public int getRampHeight() {
		int distance = depthSensor.getValue();
		return distance;
	}
	
	public void moveUp() {
		if (!topSensor.get()) {
			System.out.println("Limit Switch: " + topSensor.get());
			if (getRampHeight() > Constants.RAMP_MOVEMENT_VERTICAL_DIST*Constants.RAMP_SLOW_DOWN_POINT) {
				firstRampMotor.set(Constants.Values.Drive.MID_RAMP_SPEED);
				secondRampMotor.set(Constants.Values.Drive.MID_RAMP_SPEED);
			}
			firstRampMotor.set(1);
			secondRampMotor.set(1);
		}
		else {
			firstRampMotor.set(0);
			secondRampMotor.set(0);
		}
	}
	
		
	public void moveDown() {
		if (!bottomSensor.get()) {
			if (getRampHeight() < Constants.RAMP_MOVEMENT_VERTICAL_DIST*Constants.RAMP_SLOW_DOWN_POINT) {
				firstRampMotor.set(-Constants.Values.Drive.MID_RAMP_SPEED);
				secondRampMotor.set(-Constants.Values.Drive.MID_RAMP_SPEED);
			}
			firstRampMotor.set(-1);
			secondRampMotor.set(-1);
		} else {
			firstRampMotor.set(0);
			secondRampMotor.set(0);
		}
	}
	
	public void moveRampManual(double speed) {
		if (!topSensor.get() && speed < 0) {
			firstRampMotor.set(speed);
			secondRampMotor.set(speed);
		} else if (!bottomSensor.get() && speed > 0) {
			firstRampMotor.set(speed);
			secondRampMotor.set(speed);
		} else {
			firstRampMotor.set(0);
			secondRampMotor.set(0);
		}
	}
	
}
