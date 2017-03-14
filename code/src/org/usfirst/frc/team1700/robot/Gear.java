/* This class handles all actuations associated with retrieving and placing gears including:
 * PWM Servos for flaps
 * Double acting pneumatic solenoid for "popper" to expand front of gear slot
 * Single acting pneumatic solenoid for "dropper" to leave gear on the peg
 */
// TODO: Add "dropper" logic


package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Gear {
	Servo leftFlapServo;
	Servo rightFlapServo;
	DoubleSolenoid popper;
	DoubleSolenoid dropper;
	DigitalInput beamBreakReceiver;
	DigitalInput beamBreakEmitter;
	
	public Gear() {
		leftFlapServo = new Servo(Constants.PWM.FLAP_LEFT.getPort());
		rightFlapServo = new Servo(Constants.PWM.FLAP_RIGHT.getPort());
		popper = new DoubleSolenoid(
				Constants.Solenoids.POPPER_1.getPort(), 
				Constants.Solenoids.POPPER_2.getPort());
		dropper = new DoubleSolenoid(
				Constants.Solenoids.DROPPER_1.getPort(),
				Constants.Solenoids.DROPPER_2.getPort());
		beamBreakReceiver = new DigitalInput(Constants.DigitalIO.GEAR_RECEIVER_SENSOR_ID.getPort());
	}
	
	// Method for each available position of flaps and set to servos
	// Gear intake/outtake position is vertical
	public void flapGearIntakePosition() {
		setServos(Constants.Values.Servos.GEAR_INTAKE_POSITION);
	}

	// Ball intake position is half deployed at an approximately 45 degree angle to cover gear slot
	public void flapBallIntakePosition() {
		setServos(Constants.Values.Servos.BALL_INTAKE_POSITION);
	}
	
	// Low goal position brings flap down to low goal height of 18"
	public void flapLowGoalPosition() {
		setServos(Constants.Values.Servos.BALL_DUMPING_POSITION);
	}
	
	private void setServos(double value) {
		// Since servos are mirrored, needed to calculated their positions separately.
		// Specific values can be found in Constants file
		leftFlapServo.set(value);
		rightFlapServo.set(Constants.Values.Servos.FLAP_SERVO_OFFSET-value);
	}
	
	// Methods to handle gear slot "popping"
	public void retractSlot(){
		popper.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void extendSlot(){
		popper.set(DoubleSolenoid.Value.kForward);
	}
	
	// Methods to handle gear slot "dropping"
	public void retractDropper(){
		dropper.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void extendDropper(){
		dropper.set(DoubleSolenoid.Value.kForward);
	}
	
//	public boolean gearInSlot() {
//		return (!beamBreakReceiver.get());
//	}
	
	// TODO: Add methods to handle gear slot "dropping"
}
