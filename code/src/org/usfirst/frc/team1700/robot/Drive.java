package org.usfirst.frc.team1700.robot;

import com.ctre.CANTalon;

public class Drive {
	CANTalon rightFront;
    CANTalon rightBack;
    CANTalon leftFront;
    CANTalon leftBack;
	
	public Drive() {
		rightFront = new CANTalon(0); //TODO: Change ID's
		rightBack = new CANTalon(1);
		leftFront = new CANTalon(2);
		leftBack = new CANTalon(3);
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(rightSpeed);
		rightBack.set(rightSpeed);
	}
}
