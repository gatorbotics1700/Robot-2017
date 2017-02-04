package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class LowGoal {
	Victor rampMotor;
	DigitalInput upSensor;
	DigitalInput downSensor;
	
	public LowGoal() {
		rampMotor = new Victor(Constants.PWM.DUMPER_MOTOR.getPort());
		upSensor = new DigitalInput(Constants.DigitalIO.UP_SENSOR_ID.getPort()); //TODO: Figure out what type of sensor we are using (if any)
		downSensor = new DigitalInput(Constants.DigitalIO.DOWN_SENSOR_ID.getPort());
	}
	
	/**
	 * Moves the ramp up or down depending on joystick input.
	 * Stops the movement when sensors are triggered.
	 * 
	 * @param joy The joystick controlling the low goal shooter ramp.
	 */

	
	public void moveUp() {
		if (!upSensor.get()) {
			rampMotor.set(1);
		}
	}
		
	public void moveDown() {
		if (!downSensor.get()) {
			rampMotor.set(-1);
		}
	}
	
	public void moveRampManual(double speed) {
		if (!upSensor.get() && !downSensor.get()) {
			rampMotor.set(speed);
		}
	}
}
