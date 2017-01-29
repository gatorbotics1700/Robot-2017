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
		leftDriveEncoder = new Encoder(Constants.QUAD_ENCODER_LEFT_1, Constants.QUAD_ENCODER_LEFT_2);
		rightDriveEncoder = new Encoder(Constants.QUAD_ENCODER_RIGHT_1, Constants.QUAD_ENCODER_RIGHT_2);
	}
	
	public Pose getCurrentPose() {
		Pose currentPose = new Pose(getCurrentAngle(), getCurrentDistance());
		return currentPose;
	}
	
	public Pose getCurrentPoseWithDelta(PoseDelta poseDelta) {
		Pose currentPose = getCurrentPose();
		currentPose.angle += poseDelta.angleDelta;
		currentPose.distance += poseDelta.distanceDelta;
		return currentPose;
	}
	
	private double getCurrentAngle() {
		return NavX.getAngle();
	}
	
	private double getCurrentDistance() {
		return Constants.ticksToInches((Math.abs(leftDriveEncoder.get()) + 
				Math.abs(rightDriveEncoder.get())/2));
	}


}
