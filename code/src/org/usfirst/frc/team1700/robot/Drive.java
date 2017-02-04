package org.usfirst.frc.team1700.robot;



import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SPI;
 

public class Drive {
	CANTalon rightFront;
    CANTalon rightBack;
    CANTalon leftFront;
    CANTalon leftBack;
    Servo leftDriveServo;
    Servo rightDriveServo;
	 
    
	public Drive() {
		leftDriveServo = new Servo(Constants.LEFT_DRIVE_SERVO_ID);
		rightDriveServo = new Servo(Constants.RIGHT_DRIVE_SERVO_ID);
		rightFront = new CANTalon(Constants.DRIVE_RIGHT_FRONT); //TODO: Change ID's
		rightBack = new CANTalon(Constants.DRIVE_RIGHT_BACK);
		leftFront = new CANTalon(Constants.DRIVE_LEFT_FRONT);
		leftBack = new CANTalon(Constants.DRIVE_LEFT_BACK);
	}
	
	public boolean driveByPoseDelta(PoseDelta poseDelta) {
		if(poseDelta.nearZero()) {
			driveTank(0,0);
			return true; 
		}
		double angleSpeed = poseDelta.angleDelta*Constants.TURNING_ANGLE_PROPORTION;
		double distanceSpeed = -poseDelta.distanceDelta*Constants.DRIVING_DISTANCE_PROPORTION;
		double rightSpeed = distanceSpeed + angleSpeed;
		rightSpeed = Math.copySign(Math.max(Constants.MIN_DRIVE_POWER, Math.abs(rightSpeed)), rightSpeed);
		double leftSpeed = distanceSpeed - angleSpeed;
		leftSpeed = Math.copySign(Math.max(Constants.MIN_DRIVE_POWER, Math.abs(leftSpeed)), leftSpeed);
		
		driveTank(leftSpeed, rightSpeed);
	
		return false;
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(-rightSpeed);
		rightBack.set(-rightSpeed);
	}
	
	public void shiftDrive(boolean highGear){
		if (highGear){
			rightDriveServo.set(Constants.DRIVE_SERVO_SHIFT_HIGH_POSITION);
			leftDriveServo.set(Constants.DRIVE_SERVO_SHIFT_HIGH_POSITION);
		} else {
			rightDriveServo.set(Constants.DRIVE_SERVO_SHIFT_LOW_POSITION);
			leftDriveServo.set(Constants.DRIVE_SERVO_SHIFT_LOW_POSITION);
		}
	}
	
}
