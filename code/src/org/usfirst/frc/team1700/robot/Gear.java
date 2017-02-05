package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo firstFlapServo;
	Servo secondFlapServo;
	DoubleSolenoid retractor;
	
	
	public Gear() {
		firstFlapServo = new Servo(Constants.PWM.FLAP_FRONT_SERVO_ID.getPort());
		secondFlapServo = new Servo(Constants.PWM.FLAP_BACK_SERVO_ID.getPort());
		retractor = new DoubleSolenoid(
				Constants.Solenoids.RETRACTOR_1.getPort(), 
				Constants.Solenoids.RETRACTOR_2.getPort());
	}
	
	public void flapGearIntakePosition() {
		setServos(Constants.Values.Servos.GEAR_INTAKE_POSITION);
	}
	
	public void flapBallIntakePosition() {
		setServos(Constants.Values.Servos.BALL_INTAKE_POSITION);
	}
	
	public void flapLowGoalPosition() {
		setServos(Constants.Values.Servos.BALL_DUMPING_POSITION);
	}
	
	private void setServos(double value) {
		firstFlapServo.set(value);
		secondFlapServo.set(1-value);
	}
	
	public void retractSlot(){
		retractor.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void extendSlot(){
		retractor.set(DoubleSolenoid.Value.kForward);
	}
	
	
}
