package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo flapServo;
	
	public Gear() {
		flapServo = new Servo(0);
	}
	
	public void moveFlap(Joystick joy) {
		if (joy.getRawButton(Constants.FLAP_DOWN)) {
			flapServo.set(0.0);
		} else if (joy.getRawButton(Constants.FLAP_UP)) {
			flapServo.set(1.0);
		}
	}
}
