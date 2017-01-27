package org.usfirst.frc.team1700.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class LowGoal {
	CANTalon rampMotor;
	DigitalInput upSensor;
	DigitalInput downSensor;
	
	public LowGoal() {
		rampMotor = new CANTalon(Constants.RAMP_MOTOR);
		upSensor = new DigitalInput(Constants.UP_SENSOR_ID); //TODO: Figure out what type of sensor we are using (if any)
		downSensor = new DigitalInput(Constants.DOWN_SENSOR_ID);
	}
	
	/**
	 * Moves the ramp up or down depending on joystick input.
	 * Stops the movement when sensors are triggered.
	 * 
	 * @param joy The joystick controlling the low goal shooter ramp.
	 */

	
	public void moveRampForLowGoal() {
		if(!upSensor.get()) {
			rampMotor.set(1);
		}
	}
		
	public void moveRampDown() {
		if (!downSensor.get()) {
			rampMotor.set(-1);
		}
	}
}
