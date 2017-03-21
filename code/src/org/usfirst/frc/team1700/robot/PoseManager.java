package org.usfirst.frc.team1700.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

public class PoseManager {
	
	AHRS NavX;
	Encoder leftDriveEncoder;
	Encoder rightDriveEncoder;
	CircularBuffer poseHistory;
	
	public PoseManager() {
		NavX = new AHRS(SPI.Port.kMXP);
		leftDriveEncoder = new Encoder(
				Constants.DigitalIO.QUAD_ENCODER_LEFT_1.getPort(), 
				Constants.DigitalIO.QUAD_ENCODER_LEFT_2.getPort(),
				true);
		rightDriveEncoder = new Encoder(
				Constants.DigitalIO.QUAD_ENCODER_RIGHT_1.getPort(), 
				Constants.DigitalIO.QUAD_ENCODER_RIGHT_2.getPort(),
				false);
		poseHistory = new CircularBuffer(Constants.Values.Drive.MILLISECOND_HISTORY_LENGTH);
		poseHistory.addInitialPose(getCurrentPose());
	}
	
	public Pose getCurrentPose() {
		Pose currentPose = new Pose(getCurrentAngle(), getCurrentDistance());
		return currentPose;
	}
	
	public void storeCurrentPose() {
		poseHistory.add(getCurrentPose());
	}
	
	private double getCurrentAngle() {
		return NavX.getAngle();
	}
	
	private double getCurrentDistance() {
		System.out.println("Left Encoder: " + leftDriveEncoder.get() + " Right Encoder: " + rightDriveEncoder.get());
		// 16.5 is half of the wheelbase width of the robot in inches. This works out to be the number of
		// encoder ticks to rotate the robot 1 degree.
		double tempEncoderAngle = (leftDriveEncoder.get()-rightDriveEncoder.get())/16.5;
		// If there an difference of greater than 20 degrees, there is an issue
		// This is an error of 330 ticks. If an encoder read 212 ticks instead of 360, this
		// will create the 330 tick error in about 2 revolutions (2 feet)
		if(Math.abs(tempEncoderAngle-getCurrentAngle())>20){
			// Encoder failure
			// Currently assume it is left encoder failure
			// Todo: add logic to detect which side fails
			return Constants.ticksToInches(rightDriveEncoder.get()) + 16.5*Math.sin(Math.toRadians(getCurrentAngle()));
		}else{
			return Constants.ticksToInches((leftDriveEncoder.get() + 
				rightDriveEncoder.get())/2);
		}
	}
	
	public void printDistance() {
		System.out.println("Left Encoder: " + leftDriveEncoder.get() + " Right Encoder: " + rightDriveEncoder.get());
	}


}
