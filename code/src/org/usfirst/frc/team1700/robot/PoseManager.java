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
		return Constants.ticksToInches((leftDriveEncoder.get() + 
				rightDriveEncoder.get())/2);
	}
	
	public void printDistance() {
		System.out.println("Left Encoder: " + leftDriveEncoder.get() + " Right Encoder: " + rightDriveEncoder.get());
	}


}
