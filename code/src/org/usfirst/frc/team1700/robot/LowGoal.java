package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class LowGoal {
	Victor rampMotor;
	DigitalInput topSensor;
	DigitalInput middleSensor;
	DigitalInput bottomSensor;
	
	public LowGoal() {
		rampMotor = new Victor(Constants.PWM.DUMPER_MOTOR.getPort());
		topSensor = new DigitalInput(Constants.DigitalIO.TOP_SENSOR_ID.getPort()); 
		middleSensor = new DigitalInput(Constants.DigitalIO.MIDDLE_SENSOR_ID.getPort());
		bottomSensor = new DigitalInput(Constants.DigitalIO.BOTTOM_SENSOR_ID.getPort());
	}
	
	/**
	 * Moves the ramp up or down depending on joystick input.
	 * Stops the movement when sensors are triggered.
	 * 
	 * @param joy The joystick controlling the low goal shooter ramp.
	 */

	
	public void moveUp() {
		if (!topSensor.get()) {
			System.out.println("Limit Switch: " + topSensor.get());
			if (!middleSensor.get()) {
				System.out.println("Beam break sensor: " + middleSensor.get());
				rampMotor.set(0.6);
			}
			rampMotor.set(1);
		}
	}
		
	public void moveDown() {
		if (!bottomSensor.get()) {
			if (!middleSensor.get()) {
				rampMotor.set(0.6);
			}
			rampMotor.set(1);
		}
	}
	
	public void moveRampManual(double speed) {
		if (!topSensor.get() && speed < 0) {
			rampMotor.set(speed);
		} else if (!bottomSensor.get() && speed > 0) {
			rampMotor.set(speed);
		} else {
			rampMotor.set(0);
		}
	}
	
	
}
