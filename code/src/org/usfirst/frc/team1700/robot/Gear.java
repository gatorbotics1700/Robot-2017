package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo leftFlapServo;
	Servo rightFlapServo;
	DoubleSolenoid retractor;
	DigitalInput beamBreakReceiver;
	DigitalInput beamBreakEmitter;
	
	
	public Gear() {
		leftFlapServo = new Servo(Constants.PWM.FLAP_LEFT.getPort());
		rightFlapServo = new Servo(Constants.PWM.FLAP_RIGHT.getPort());
		retractor = new DoubleSolenoid(
				Constants.Solenoids.RETRACTOR_1.getPort(), 
				Constants.Solenoids.RETRACTOR_2.getPort());
		beamBreakReceiver = new DigitalInput(Constants.DigitalIO.GEAR_RECEIVER_SENSOR_ID.getPort());
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
		leftFlapServo.set(value);
		rightFlapServo.set(Constants.Values.Servos.FLAP_SERVO_OFFSET-value);
	}
	
	public void retractSlot(){
		retractor.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void extendSlot(){
		retractor.set(DoubleSolenoid.Value.kForward);
	}
	
	public boolean gearInSlot() {
		return (!beamBreakReceiver.get());
	}
}
