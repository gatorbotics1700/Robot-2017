package org.usfirst.frc.team1700.robot;

public class LeftPegAutonomousWithoutVision extends SidePegAutonomousWithoutVision {

	LeftPegAutonomousWithoutVision(DriveTrain drive, PoseManager poseManager, Gear gear) {
		super(drive, poseManager, gear);
		turnAngle = Constants.Values.Auto.TURN_ANGLE;
	}
}
