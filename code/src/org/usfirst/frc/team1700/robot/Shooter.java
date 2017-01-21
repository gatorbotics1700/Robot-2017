package org.usfirst.frc.team1700.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class Shooter {
	CANTalon rampMotor;
	DigitalInput upSensor;
	DigitalInput downSensor;
	
	public Shooter() {
		rampMotor = new CANTalon(3);
		upSensor = new DigitalInput(1);
		downSensor = new DigitalInput(2);
	}
	
	public void moveRamp(Joystick joy) {
		if (joy.getRawButton(Constants.RAMP_UP)) {
			if (!upSensor.get()) {
				rampMotor.set(1);
			}
		} else if (joy.getRawButton(Constants.RAMP_DOWN)) {
			if (!downSensor.get()) {
				rampMotor.set(-1);
			}
		}
	}
}
