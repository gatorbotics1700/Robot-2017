package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo firstFlapServo;
	Servo secondFlapServo;
	
	
	public Gear() {
		firstFlapServo = new Servo(Constants.FLAP_FRONT_SERVO_ID);
		secondFlapServo = new Servo(Constants.FLAP_BACK_SERVO_ID);
	}
	
	/**
	 * Moves the flap of the gear mechanism up or down with a servo motor.
	 * 
	 * @param joy The joystick controlling the gear mechanism.
	 */
	
	public void gearIntakePosition() {
		setServos(Constants.GEAR_INTAKE_POSITION);
	}
	
	public void ballIntakePosition() {
		setServos(Constants.BALL_INTAKE_POSITION);
	}
	
	public void lowGoalPosition() {
		setServos(Constants.BALL_DUMPING_POSITION);
	}
	
	private void setServos(double value) {
		firstFlapServo.set(value);
		secondFlapServo.set(1-value);
	}
}
