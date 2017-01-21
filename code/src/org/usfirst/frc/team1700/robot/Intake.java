package org.usfirst.frc.team1700.robot;

import com.ctre.CANTalon;

public class Intake {
	CANTalon intakeMotor;
	
	public Intake(){
		intakeMotor = new CANTalon(1);
	}
	
	public void runIntake(){
		intakeMotor.set(1);
	}
}