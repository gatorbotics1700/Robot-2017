package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo flapServo;
	
	public Gear() {
		flapServo = new Servo(Constants.GEAR_SERVO);
	}
	
	public void moveFlap(Joystick joy) {
		if (joy.getRawButton(Constants.FLAP_DOWN)) {
			flapServo.set(Constants.GEAR_SERVO_DOWN);
		} else if (joy.getRawButton(Constants.FLAP_UP)) {
			flapServo.set(Constants.GEAR_SERVO_UP);
		}
	}
}
