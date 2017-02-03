package org.usfirst.frc.team1700.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

public class PoseManager {
	
	AHRS NavX;
	Encoder leftDriveEncoder;
	Encoder rightDriveEncoder;
	
	public PoseManager() {
		NavX = new AHRS(SPI.Port.kMXP);
		leftDriveEncoder = new Encoder(Constants.QUAD_ENCODER_LEFT_1, Constants.QUAD_ENCODER_LEFT_2, true);
		rightDriveEncoder = new Encoder(Constants.QUAD_ENCODER_RIGHT_1, Constants.QUAD_ENCODER_RIGHT_2, false);
	}
	
	public Pose getCurrentPose() {
		Pose currentPose = new Pose(getCurrentAngle(), getCurrentDistance());
		return currentPose;
	}
	
	private double getCurrentAngle() {
		return NavX.getAngle();
	}
	
	private double getCurrentDistance() {
		System.out.println(leftDriveEncoder.get() + " " + rightDriveEncoder.get());
		return Constants.ticksToInches((leftDriveEncoder.get() + 
				rightDriveEncoder.get())/2);
	}


}
