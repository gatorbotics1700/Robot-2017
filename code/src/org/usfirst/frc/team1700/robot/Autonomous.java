package org.usfirst.frc.team1700.robot;

import org.usfirst.frc.team1700.robot.Robot.AutoStage;

public abstract class Autonomous {
	private DriveTrain drive;
	private PoseManager poseManager;
	private Pose destinationPose;
    protected CameraData cameraData;
	
	public Autonomous(DriveTrain drive, PoseManager poseManager) {
		this.drive = drive;
		this.poseManager = poseManager;
		init();
	}
	
	public void update() {
		PoseDelta delta = destinationPose.subtract(poseManager.getCurrentPose());
		periodic(drive.driveByPoseDelta(delta, false));
		// TODO: Set all robot state (shifting, intake, ramp, etc)
		// TODO: Store robot pose history here
		// TODO: Store camera data in this class, update here.
	}
	
	protected void updateDestination(PoseDelta delta) {
		destinationPose = poseManager.getCurrentPose().add(delta);
	}
	
	protected void resetCameraData() {
		cameraData.timestamp = System.currentTimeMillis();
		cameraData.angle = 0;
	}
	
	protected abstract void init();
	protected abstract void periodic(boolean atDestination, boolean newCameraData);
	
	
}
