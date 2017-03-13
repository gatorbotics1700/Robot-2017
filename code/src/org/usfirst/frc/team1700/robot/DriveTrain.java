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
    DoubleSolenoid shifter;
    
	public DriveTrain() {
		rightFront = new CANTalon(Constants.CanBus.DRIVE_RIGHT_FRONT.getId()); 
		rightBack = new CANTalon(Constants.CanBus.DRIVE_RIGHT_BACK.getId());
		leftFront = new CANTalon(Constants.CanBus.DRIVE_LEFT_FRONT.getId());
		leftBack = new CANTalon(Constants.CanBus.DRIVE_LEFT_BACK.getId());
		shifter = new DoubleSolenoid(
				Constants.Solenoids.SHIFTER_1.getPort(),
				Constants.Solenoids.SHIFTER_2.getPort());

	}
	
	public void setDriveVoltageRampRates() {
		rightFront.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
		rightBack.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
		leftFront.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
		leftBack.setVoltageRampRate(Constants.Values.Drive.VOLTAGE_RAMP_RATE);
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
		leftSpeed *= 1.2;  
		System.out.println("Delta: " + poseDelta);
		System.out.println("Portions: " + angleSpeed + ", " + distanceSpeed);
		System.out.println("Speeds:" + leftSpeed + ", " + rightSpeed);
		driveTank(leftSpeed, rightSpeed);
	
		return false;
	}
	
	public boolean driveByPoseDeltaLowGear(PoseDelta poseDelta) {
		if(poseDelta.nearZero()) {
			driveTank(0,0);
			return true; 
		}
		double angleSpeed = poseDelta.angleDelta*Constants.Values.Drive.TURNING_ANGLE_PROPORTION_LOW_GEAR;
		double distanceSpeed = -poseDelta.distanceDelta*Constants.Values.Drive.DRIVING_DISTANCE_PROPORTION_LOW_GEAR;
		double rightSpeed = distanceSpeed + angleSpeed;
		rightSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER_LOW_GEAR, Math.abs(rightSpeed)), rightSpeed);
		double leftSpeed = distanceSpeed - angleSpeed;
		leftSpeed = Math.copySign(Math.max(Constants.Values.Drive.MIN_DRIVE_POWER_LOW_GEAR, Math.abs(leftSpeed)), leftSpeed);
		leftSpeed *= 1.2;  
		System.out.println("Delta: " + poseDelta);
		System.out.println("Portions: " + angleSpeed + ", " + distanceSpeed);
		System.out.println("Speeds:" + leftSpeed + ", " + rightSpeed);
		driveTank(leftSpeed, rightSpeed);
	
		return false;
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(-rightSpeed);
		rightBack.set(-rightSpeed);
	}
	
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
	
	public void printClimbCurrent() {
		System.out.println("Left Current 1: " + leftBack.getOutputCurrent());
		System.out.println("Left Current 2: " + leftFront.getOutputCurrent());
	}
	
	public void shiftDrive() {
		if (shifter.get() == DoubleSolenoid.Value.kReverse) {
			shifter.set(DoubleSolenoid.Value.kForward);
		} else {
			shifter.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
}
