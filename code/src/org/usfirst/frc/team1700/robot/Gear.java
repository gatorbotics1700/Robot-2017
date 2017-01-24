package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo flapServo;
	
	public Gear() {
		flapServo = new Servo(Constants.GEAR_SERVO_ID);
	}
	
	/**
	 * Moves the flap of the gear mechanism up or down with a servo motor.
	 * 
	 * @param joy The joystick controlling the gear mechanism.
	 */
	public void moveFlap(Joystick joy) {
		if (joy.getRawButton(Constants.FLAP_DOWN)) {
			flapServo.set(Constants.GEAR_SERVO_CLOSED_POSITION);
		} else if (joy.getRawButton(Constants.FLAP_UP)) {
			flapServo.set(Constants.GEAR_SERVO_OPEN_POSITION);
		}
	}
}
