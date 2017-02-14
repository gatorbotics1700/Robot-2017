package org.usfirst.frc.team1700.robot;

public class LeftPegAutonomous extends SidePegAutonomous {
	
	LeftPegAutonomous(DriveTrain drive, PoseManager poseManager) {
		super(drive, poseManager);
		turnAngle = Constants.Values.Auto.TURN_ANGLE;
	}
}
