/* This class handles all actuations associated with the drivetrain including:
 * One single acting pneumatic solenoid for shifting
 * Four CAN Talons for CIM motor control
 */

package org.usfirst.frc.team1700.robot;


import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
 

public class DriveTrain {
	CANTalon rightFront;
    CANTalon rightBack;
    CANTalon leftFront;
    CANTalon leftBack;
    DoubleSolenoid shifter;
    int goalCounter;
    boolean nearZero;
    
	public DriveTrain() {
		// Two motors/controllers per side of the robot
		rightFront = new CANTalon(Constants.CanBus.DRIVE_RIGHT_FRONT.getId()); 
		rightBack = new CANTalon(Constants.CanBus.DRIVE_RIGHT_BACK.getId());
		leftFront = new CANTalon(Constants.CanBus.DRIVE_LEFT_FRONT.getId());
		leftBack = new CANTalon(Constants.CanBus.DRIVE_LEFT_BACK.getId());
		shifter = new DoubleSolenoid(
				Constants.Solenoids.SHIFTER_1.getPort(),
				Constants.Solenoids.SHIFTER_2.getPort());
		
		goalCounter = 0;
		boolean nearZero = false;
	}
	
	// Voltage ramp rate used to prevent brownouts
	// Note: potentially replaced by using max current functionality on talons
	public void setDriveVoltageRampRates() {
		rightFront.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
		rightBack.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
		leftFront.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
		leftBack.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
	}
	
	// Relative pose based driving. Use to go to set points in autonomous
//	public boolean driveByPoseDelta(PoseDelta poseDelta) {
//		double angleSpeed, distanceSpeed;
//		double linearVelocity = (leftDriveEncoder.getRate() + rightDriveEncoder.getRate())/2;
//		linearVelocity = Constants.inchesToTicks(linearVelocity);
//		double angularVelocity = NavX.getYaw();
//		if(poseDelta.nearZero()){
//			System.out.println("Stop robot");
//			driveTank(0,0);
//			goalCounter = 25;
//			return true;
//		}else{
//			double tempAngleSpeed = poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION + angularVelocity*Constants.Values.Drive.TURNING_ANGLE_D;
//			// caps angle speed at max angle speed value
//			if(tempAngleSpeed > 
//			Constants.Values.Drive.MAX_ANGLE_SPEED) {
//				angleSpeed = Constants.Values.Drive.MAX_ANGLE_SPEED;
//			} else if(tempAngleSpeed < 
//			-Constants.Values.Drive.MAX_ANGLE_SPEED){
//				angleSpeed = -Constants.Values.Drive.MAX_ANGLE_SPEED;				
//			} else {
//				angleSpeed = tempAngleSpeed;
//			}
//			double tempLinearSpeed = poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION + linearVelocity*Constants.Values.Drive.DRIVING_DISTANCE_D ;
//			// caps distance speed at max distance speed value
//			if(tempLinearSpeed > 
//			Constants.Values.Drive.MAX_DISTANCE_SPEED) {
//				distanceSpeed = -Constants.Values.Drive.MAX_DISTANCE_SPEED;
//			} else if(tempLinearSpeed < 
//			-Constants.Values.Drive.MAX_DISTANCE_SPEED) {
//				distanceSpeed = Constants.Values.Drive.MAX_DISTANCE_SPEED;
//			} else {
//				distanceSpeed = -tempLinearSpeed;
//			}
//			
//			double rightSpeed = distanceSpeed + angleSpeed;
//			// Min drive power used to overcome static frictions
//			rightSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(rightSpeed)), rightSpeed);
//			double leftSpeed = distanceSpeed - angleSpeed;
//			leftSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(leftSpeed)), leftSpeed);
//			leftSpeed *= 1; // Compensating multiplier for differences in friction. This is largely due to PTO  
//			System.out.println("Delta: " + poseDelta);
//			System.out.println("Portions: " + angleSpeed + ", " + distanceSpeed);
//			System.out.println("Speeds:" + leftSpeed + ", " + rightSpeed);
//			driveTank(leftSpeed, rightSpeed);
//			if(poseDelta.withinTol()) {
//				 goalCounter++;
//				 if(goalCounter>25)
//					 return true;
//			}else{
//				goalCounter = 0;
//			}	
//		}
//		return false;
//	}
	
	public boolean driveByPoseDelta(PoseDelta poseDelta) {
		double angleSpeed, distanceSpeed;
//		double linearVelocity = (leftDriveEncoder.getRate() + rightDriveEncoder.getRate())/2;
//		double angularVelocity = NavX.getYaw();
		if(poseDelta.nearZero()){
			System.out.println("Stop robot");
			driveTank(0,0);
			goalCounter = 25;
			return true;
		}else{
			// caps angle speed at max angle speed value
			if(poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION > 
			Constants.Values.Drive.MAX_ANGLE_SPEED) {
				angleSpeed = Constants.Values.Drive.MAX_ANGLE_SPEED;
			} else if(poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION < 
			-Constants.Values.Drive.MAX_ANGLE_SPEED){
				angleSpeed = -Constants.Values.Drive.MAX_ANGLE_SPEED;				
			} else {
				angleSpeed = poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION;
			}
			
			// caps distance speed at max distance speed value
			if(poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION > 
			Constants.Values.Drive.MAX_DISTANCE_SPEED) {
				distanceSpeed = -Constants.Values.Drive.MAX_DISTANCE_SPEED;
			} else if(poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION < 
			-Constants.Values.Drive.MAX_DISTANCE_SPEED) {
				distanceSpeed = Constants.Values.Drive.MAX_DISTANCE_SPEED;
			} else {
				distanceSpeed = -poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION;
			}
			
			double rightSpeed = distanceSpeed + angleSpeed;
			// Min drive power used to overcome static frictions
			rightSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(rightSpeed)), rightSpeed);
			double leftSpeed = distanceSpeed - angleSpeed;
			leftSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(leftSpeed)), leftSpeed);
			leftSpeed *= 1; // Compensating multiplier for differences in friction. This is largely due to PTO  
			System.out.println("Delta: " + poseDelta);
			System.out.println("Portions: " + angleSpeed + ", " + distanceSpeed);
			System.out.println("Speeds:" + leftSpeed + ", " + rightSpeed);
			driveTank(leftSpeed, rightSpeed);
			if(poseDelta.withinTol()) {
				 goalCounter++;
				 if(goalCounter>25)
					 return true;
			}else{
				goalCounter = 0;
			}	
		}
		return false;
	}
	
	
	// Relative pose based driving. Use to go to set points in autonomous
	// Separate since different frictions/losses associated with low gear
	public boolean driveByPoseDeltaLowGear(PoseDelta poseDelta) {
		if(poseDelta.nearZero()) {
			driveTank(0,0);
			return true; 
		}
		
		double angleSpeed = poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION_LOW_GEAR;
		double distanceSpeed = -poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION_LOW_GEAR;
		double rightSpeed = distanceSpeed + angleSpeed;
		// Min drive power used to overcome static frictions
		rightSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER_LOW_GEAR, Math.abs(rightSpeed)), rightSpeed);
		double leftSpeed = distanceSpeed - angleSpeed;
		leftSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER_LOW_GEAR, Math.abs(leftSpeed)), leftSpeed);
		leftSpeed *= 1.2; // Compensating multiplier for differences in friction. This is largely due to PTO  
		System.out.println("Delta: " + poseDelta);
		System.out.println("Portions: " + angleSpeed + ", " + distanceSpeed);
		System.out.println("Speeds:" + leftSpeed + ", " + rightSpeed);
		driveTank(leftSpeed, rightSpeed);
	
		return false;
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		if(leftSpeed < 0)
			leftSpeed = leftSpeed - 0.2;
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(-rightSpeed);
		rightBack.set(-rightSpeed);
	}
	
	// remaps joystick input for driver comfort
	public void driveTankTuned(double leftSpeed, double rightSpeed) {
		double tunedLeftSpeed = Constants.Values.Drive.JOYSTICK_TUNING_CONSTANT*Math.pow(leftSpeed, 3) + (1-Constants.Values.Drive.JOYSTICK_TUNING_CONSTANT)*leftSpeed;
		double tunedRightSpeed = Constants.Values.Drive.JOYSTICK_TUNING_CONSTANT*Math.pow(rightSpeed, 3) + (1-Constants.Values.Drive.JOYSTICK_TUNING_CONSTANT)*rightSpeed;
		leftFront.set(tunedLeftSpeed);
		leftBack.set(tunedLeftSpeed);
		rightFront.set(-tunedRightSpeed);
		rightBack.set(-tunedRightSpeed);
	}
	
	public boolean driveTankByAngle(double speed, double angle) {
		PoseDelta delta = new PoseDelta(angle, 0);
		double angleSpeed = angle*Constants.Values.Drive.TURNING_ANGLE_PROPORTION;
		if(Math.abs(speed) < Constants.Values.Drive.MIN_DRIVE_POWER) {
			angleSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(angleSpeed)), angleSpeed);
		}
		if(delta.nearZero()) {
			angleSpeed = 0;
		}
		
		double right = speed + angleSpeed;
		//right = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(right)), right);
		double left = speed - angleSpeed;
		//left = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER, Math.abs(left)), left);
		right /= 1.2;  
		System.out.println("Angle: " + angle);
		driveTank(left, right);
	
		return false;
	}
	
	public void shiftDriveHigh(boolean highGear){
		if (highGear) {
			shifter.set(DoubleSolenoid.Value.kForward);
		} else {
			shifter.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	
	
	public void shiftDrive() {
		if (shifter.get() == DoubleSolenoid.Value.kReverse) {
			shifter.set(DoubleSolenoid.Value.kForward);
		} else {
			shifter.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	public void setMaxCurrent() {
		leftBack.setCurrentLimit(Constants.Values.Climb.MAX_CURRENT);
		leftFront.setCurrentLimit(Constants.Values.Climb.MAX_CURRENT);
	}
	
}
