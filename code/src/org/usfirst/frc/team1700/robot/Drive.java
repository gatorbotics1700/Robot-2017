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
    PIDController leftFrontDistController;
    PIDController leftBackDistController;
    PIDController rightFrontDistController;
    PIDController rightBackDistController;
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
		
//		NavX = new AHRS(SPI.Port.kMXP);
//		leftEncoder = new Encoder(Constants.QUAD_ENCODER_LEFT_1, Constants.QUAD_ENCODER_LEFT_2, true);
//		rightEncoder = new Encoder(Constants.QUAD_ENCODER_RIGHT_1, Constants.QUAD_ENCODER_RIGHT_2);
		mode = operationMode.MANUAL;
		leftFrontDistController = new PIDController(Constants.DIST_P, Constants.DIST_I, Constants.DIST_D, leftEncoder, leftFront);
		leftBackDistController = new PIDController(Constants.DIST_P, Constants.DIST_I, Constants.DIST_D, leftEncoder, leftBack);
		rightFrontDistController = new PIDController(Constants.DIST_P, Constants.DIST_I, Constants.DIST_D, rightEncoder, rightFront);
		rightBackDistController = new PIDController(Constants.DIST_P, Constants.DIST_I, Constants.DIST_D, rightEncoder, rightBack);
	}
	
	public boolean driveByPoseDelta(PoseDelta poseDelta) {
		if(poseDelta.nearZero()) {
			driveTank(0,0);
			return true;
		}
		double angleSpeed = poseDelta.angleDelta*Constants.TURNING_ANGLE_PROPORTION;
		double distanceSpeed = poseDelta.distanceDelta*Constants.DRIVING_DISTANCE_PROPORTION;
		double rightSpeed = distanceSpeed - angleSpeed;
		double leftSpeed = distanceSpeed + angleSpeed;
		
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
//			rightDriveServo.set(Constants.DRIVE_SERVO_SHIFT_HIGH_POSITION);
//			leftDriveServo.set(Constants.DRIVE_SERVO_SHIFT_HIGH_POSITION);
		} else {
			rightDriveServo.set(Constants.DRIVE_SERVO_SHIFT_LOW_POSITION);
//			leftDriveServo.set(Constants.DRIVE_SERVO_SHIFT_LOW_POSITION);
		}
	}
	
	/**
	 * Converts angle the robot needs to turn to into robot movement 
	 * by changing speed of motors and using a proportion that needs to be tuned
	 * @param angleDelta difference of angle to the target from the robot's current angle
	 * If angle delta is positive, turns robot to the right. If angle delta is negative,
	 * turns robot to the left.
	 */
	private void turnByAngle(double angleDelta) {
		double speed = angleDelta*Constants.TURNING_ANGLE_PROPORTION;
		System.out.println("Speed: " + speed);
		System.out.println("Angle delta: " + angleDelta);
		driveTank(speed, -speed);
	}
	
	/**
	 * Calculates the robot's speed based on the desired change in distance. 
	 * @param distanceDelta Desired change in distance. 
	 */
	private void driveToDistance(double distanceDelta) {
		double speed = distanceDelta*Constants.DRIVING_DISTANCE_PROPORTION;
		double angle = NavX.getAngle();
		if (angle < -3) {
			driveTank(speed, -speed/angle);
		} else if (angle > 3) {
			driveTank(-speed/angle, speed);
		} else {
			driveTank(speed, speed);
		} 
	}
	
	/**
	 * Sets target to angle that we want the robot to turn to
	 * @param angleDelta The angle that the robot wants to turn from the robot's current angle 
	 * according to the gyro on the NavX
	 */
	public void setTargetAngleDelta(double angleDelta) {
		mode = operationMode.ANGLE; 
		target = getCurrentAngle() + angleDelta; 
		System.out.println("Angle Delta: " + angleDelta);
	}
	
	/**
	 * Gets current angle of the robot from the gyroscope on the NavX
	 * @return currentAngle in degrees
	 */
	public double getCurrentAngle() {
		double currentAngle = NavX.getAngle();
		System.out.println("Current angle: " + currentAngle);
		return currentAngle;
	}
	
	/** 
	 * @return The average of two encoder values in inches. 
	 */
	public double getCurrentDistance() {
		System.out.println("Left Encoder Value: " + leftEncoder.get());
		System.out.println("Right Encoder Value: " + rightEncoder.get());
		return Constants.ticksToInches((Math.abs(leftEncoder.get())+Math.abs(rightEncoder.get())/2));
	}
	
	/**
	 * Calculates the distance to move in inches using the current position and given change in distance.
	 * @param deltaDistance Desired distance in inches. 
	 */
	public void setTargetDistance(double deltaDistance) {
		double position = getCurrentDistance();
		target = position + deltaDistance; 
		driveToDistance(deltaDistance);

		mode = operationMode.DISTANCE;
	}
	
	/**
	 * Switches between angle, manual, and distance modes by changing what gear the drive is in
	 * as well as how fast the robot should drive and what angle it should turn to. 
	 * @param leftSpeed The speed for the left motor if in manual mode. 
	 * @param rightSpeed The speed for the right motor if in manual mode. 
	 * @param highGear Boolean of whether the drive should be in high gear or not. 
	 */
	public void update(double leftSpeed, double rightSpeed, boolean highGear) {
		switch(mode) {
		case ANGLE:
			shiftDrive(true);
			turnByAngle(target-getCurrentAngle());
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
	
	public void resetNavX() {
		NavX.reset();
	}
	
	/**
	 * @return A boolean of whether the angle to the target is 0, and therefore the robot is centered on the target. 
	 */
	public boolean atTarget() {
		switch(mode) {
		case ANGLE:
			return Math.abs(target - getCurrentAngle()) < Constants.TARGET_ANGLE_TOLERANCE;
		case DISTANCE:
			System.out.println("Delta distance: " + (target - getCurrentDistance()));
			return Math.abs(target-getCurrentDistance()) < Constants.DEADBAND_DISTANCE;
		}
		return false;
	}
	
	/**
	 * Reset encoder values.
	 */
	public void zeroEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
}
