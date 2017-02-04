package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.Victor;

public class Intake {
	Victor frontRoller;
	Victor backRoller;
	
	public Intake(){
		frontRoller = new Victor(Constants.PWM.FRONT_ROLLER.getPort());
		backRoller = new Victor(Constants.PWM.BACK_ROLLER.getPort());
	}
	
	public void runIntake(){
		frontRoller.set(1);
		backRoller.set(1);
	}
	
	
	public void stopIntake(){
		frontRoller.set(0);
		backRoller.set(0);
	}
	
	public void lowGoalIntake() {
		frontRoller.set(0);
		backRoller.set(-1);
	}
	
	public void climbIntake() {
		frontRoller.set(Constants.INTAKE_CLIMBING_SPEED);
		backRoller.set(0);
	}
}

