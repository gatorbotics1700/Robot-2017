package org.usfirst.frc.team1700.robot;

import org.usfirst.frc.team1700.robot.Robot.AutoStage;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public abstract class Autonomous {
	protected DriveTrain drive;
	protected PoseManager poseManager;
	private Pose destinationPose;
	private Intake intake;
	private LowGoal lowGoal;
	protected Gear gear;	
    NetworkTable table;
    protected PoseDelta delta;
	
    protected CameraData cameraData;
	
	public Autonomous(DriveTrain drive, PoseManager poseManager, Gear gear) {
		this.drive = drive;
		this.poseManager = poseManager;
		this.gear = gear;
		init();
	}
	
	public void update() {
		delta = destinationPose.subtract(poseManager.getCurrentPose());
		periodic(drive.driveByPoseDelta(delta), false);
		intake.runIntake();
		drive.shiftDriveHigh(true);
		lowGoal.moveDown();
		gear.retractSlot();
		gear.flapGearIntakePosition();
		
		poseManager.storeCurrentPose(); //lol
		
		if(updateCameraData()) {
			cameraData = getCameraDataValues();
		}
		// TODO: Store robot pose history here
		// TODO: Store camera data in this class, update here.
	}
	
	protected void updateDestination(PoseDelta delta) {
		destinationPose = poseManager.getCurrentPose().add(delta);
	}
	
	protected CameraData getCameraDataValues() {
		double angle = table.getNumber("Angle", 0);
		long timestamp = (long) table.getNumber("Time", 0);
		double distance = table.getNumber("Distance", 0);
		return new CameraData(timestamp, angle, distance);
}
	
	 //Updates values if the values are new

	protected boolean updateCameraData() {
			CameraData newCameraData = getCameraDataValues();
			if(newCameraData.timestamp > cameraData.timestamp) {
				cameraData = newCameraData;
				return true;
			}
			return false;
	}
	
	protected void resetCameraData() {
		cameraData.timestamp = System.currentTimeMillis();
		cameraData.angle = 0;
	}
	
	protected void stopRobot() {
		drive.driveTank(0, 0);
	}
	
	protected abstract void init();
	protected abstract void periodic(boolean atDestination, boolean newCameraData);
	
	
}
