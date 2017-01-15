package org.usfirst.frc.team1700.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Drive {
	CANTalon rightFront;
    CANTalon rightBack;
    CANTalon leftFront;
    CANTalon leftBack;
    PowerDistributionPanel pdp;
	
	public Drive() {
		rightFront = new CANTalon(0); //TODO: Change ID's
		rightBack = new CANTalon(1);
		leftFront = new CANTalon(2);
		leftBack = new CANTalon(3);
		pdp = new PowerDistributionPanel(4);
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(rightSpeed);
		rightBack.set(rightSpeed);
		System.out.println("Right front motor current: " + pdp.getCurrent(0));
		System.out.println("Right back motor current: " + pdp.getCurrent(1));
		System.out.println("Left front motor current: " + pdp.getCurrent(2));
		System.out.println("Left back motor current: " + pdp.getCurrent(3));
	}
}
