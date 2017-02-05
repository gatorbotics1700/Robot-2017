package org.usfirst.frc.team1700.robot;



import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI;
 

public class DriveTrain {
	CANTalon rightFront;
    CANTalon rightBack;
    CANTalon leftFront;
    CANTalon leftBack;
    Servo leftDriveServo;
    Servo rightDriveServo;
    DoubleSolenoid shifter;
  
	 
    
	public DriveTrain() {
		leftDriveServo = new Servo(Constants.PWM.LEFT_DRIVE_SERVO_ID.getPort());
		rightDriveServo = new Servo(Constants.PWM.RIGHT_DRIVE_SERVO_ID.getPort());
		rightFront = new CANTalon(Constants.CanBus.DRIVE_RIGHT_FRONT.getId()); 
		rightBack = new CANTalon(Constants.CanBus.DRIVE_RIGHT_BACK.getId());
		leftFront = new CANTalon(Constants.CanBus.DRIVE_LEFT_FRONT.getId());
		leftBack = new CANTalon(Constants.CanBus.DRIVE_LEFT_BACK.getId());
		shifter = new DoubleSolenoid(
				Constants.Solenoids.SHIFTER_1.getPort(),
				Constants.Solenoids.SHIFTER_2.getPort());

	}
	
	public boolean driveByPoseDelta(PoseDelta poseDelta) {
		if(poseDelta.nearZero()) {
			driveTank(0,0);
			return true; 
		}
		double angleSpeed = poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION;
		double distanceSpeed = -poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION;
		double rightSpeed = distanceSpeed + angleSpeed;
		rightSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(rightSpeed)), rightSpeed);
		double leftSpeed = distanceSpeed - angleSpeed;
		leftSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(leftSpeed)), leftSpeed);
		
		driveTank(leftSpeed, rightSpeed);
	
		return false;
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(-rightSpeed);
		rightBack.set(-rightSpeed);
	}
	
	public void shiftHigh(){
		rightDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_HIGH_POSITION);
		leftDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_HIGH_POSITION);
		shifter.set(DoubleSolenoid.Value.kForward);
	}
	
	public void shiftLow(){
		rightDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_LOW_POSITION);
		leftDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_LOW_POSITION);
		shifter.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void shiftDriveHigh(boolean highGear){
		if (highGear){
			rightDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_HIGH_POSITION);
			leftDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_HIGH_POSITION);
			shifter.set(DoubleSolenoid.Value.kForward);
			
		} else {
			rightDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_LOW_POSITION);
			leftDriveServo.set(Constants.Values.Servos.DRIVE_SERVO_SHIFT_LOW_POSITION);
			shifter.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
}
