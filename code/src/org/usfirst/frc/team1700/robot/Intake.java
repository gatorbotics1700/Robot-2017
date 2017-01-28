package org.usfirst.frc.team1700.robot;

import com.ctre.CANTalon;

public class Intake {
	CANTalon frontRoller;
	CANTalon backRoller;
	public Intake(){
		frontRoller = new CANTalon(Constants.FRONT_ROLLER);
		backRoller = new CANTalon(Constants.BACK_ROLLER);
	}
	public void runIntake(){
		frontRoller.set(1);
		backRoller.set(1);
	}
	public void stopIntake(){
		frontRoller.set(0);
		backRoller.set(0);
	}
}

