package org.usfirst.frc.team1700.robot;

public class RightPegAutonomousWithoutVision extends SidePegAutonomousWithoutVision {
	RightPegAutonomousWithoutVision(DriveTrain drive, PoseManager poseManager, Gear gear) {
		super(drive, poseManager, gear);
		turnAngle = -Constants.Values.Auto.TURN_ANGLE;
	}
}
