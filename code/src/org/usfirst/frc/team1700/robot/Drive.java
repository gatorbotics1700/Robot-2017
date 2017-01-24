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
    AHRS NavX;
    PIDController turnController; 
    operationMode mode; 
    static double target;
    Encoder leftEncoder;
    Encoder rightEncoder; 
	
    enum operationMode {
    	MANUAL,
    	DISTANCE,
    	ANGLE;
    }
    
	public Drive() {
		leftDriveServo = new Servo(Constants.LEFT_DRIVE_SERVO_ID);
		rightDriveServo = new Servo(Constants.RIGHT_DRIVE_SERVO_ID);
		rightFront = new CANTalon(Constants.DRIVE_RIGHT_FRONT); //TODO: Change ID's
		rightBack = new CANTalon(Constants.DRIVE_RIGHT_BACK);
		leftFront = new CANTalon(Constants.DRIVE_LEFT_FRONT);
		leftBack = new CANTalon(Constants.DRIVE_LEFT_BACK);
		NavX = new AHRS(SPI.Port.kMXP);
		leftEncoder = new Encoder(Constants.QUAD_ENCODER_LEFT_1, Constants.QUAD_ENCODER_LEFT_2, true);
		rightEncoder = new Encoder(Constants.QUAD_ENCODER_RIGHT_1, Constants.QUAD_ENCODER_RIGHT_2);
		mode = operationMode.MANUAL;
	}
	
	private void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(rightSpeed);
		rightBack.set(rightSpeed);
	}
	
	private void shiftDrive(boolean highGear){
		if (highGear){
			rightDriveServo.set(Constants.DRIVE_SERVO_SHIFT_HIGH_POSITION);
			leftDriveServo.set(Constants.DRIVE_SERVO_SHIFT_HIGH_POSITION);
		} else {
			rightDriveServo.set(Constants.DRIVE_SERVO_SHIFT_LOW_POSITION);
			leftDriveServo.set(Constants.DRIVE_SERVO_SHIFT_LOW_POSITION);
		}
	}
	
	private void turnByAngle(double angleDelta) {
		double speed = angleDelta*Constants.TURNING_ANGLE_PROPORTION;
		if (angleDelta > 0) {
			driveTank(-speed, speed);
		} else if (angleDelta < 0) {
			driveTank(speed, -speed);
		}
	}
	
	private void driveToDistance(double distanceDelta) {
		double speed = distanceDelta*Constants.DRIVING_DISTANCE_PROPORTION;
		driveTank(-speed,speed);
	}
	
	public void setTargetAngleDelta(double angleDelta) {
		mode = operationMode.ANGLE; 
		target = getCurrentAngle(); 
		System.out.println("Angle Delta: " + angleDelta);
	}
	
	public double getCurrentAngle() {
		double currentAngle = NavX.getAngle();
		System.out.println("Current angle: " + currentAngle);
		return currentAngle;
	}
	
	public double getCurrentDistance() {
		System.out.println("Left Encoder Value: " + leftEncoder.get());
		System.out.println("Right Encoder Value: " + rightEncoder.get());
		return Constants.ticksToInches((Math.abs(leftEncoder.get())-Math.abs(rightEncoder.get())/2));
	}
	
	public void setTargetDistance(double deltaDistance) {
		double position = getCurrentDistance();
		System.out.println("Delta distance: " + deltaDistance);
		target = position + deltaDistance; 
//		System.out.println("Target distance: " + target);
		driveToDistance(deltaDistance);

		mode = operationMode.DISTANCE;
	}
	
	public void update(double leftSpeed, double rightSpeed, boolean highGear) {
		switch(mode) {
		case ANGLE:
			shiftDrive(true);
			turnByAngle(getCurrentAngle()-target);
			break;
		case MANUAL:
			shiftDrive(highGear);
			driveTank(leftSpeed, rightSpeed);	
			break;
		case DISTANCE:
			shiftDrive(true);
			System.out.println("Current distance: " + getCurrentDistance());
			System.out.println("Target distance: " + target);
			driveToDistance(target-getCurrentDistance());
			break;
		}
		if(atTarget()) {
			mode = operationMode.MANUAL;
		}
		
	}
	
	public boolean atTarget() {
		switch(mode) {
		case ANGLE:
			return Math.abs(target - getCurrentAngle()) < Constants.TARGET_ANGLE_TOLERANCE;
		}
		return false;
	}
	
	public void zeroEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
}
