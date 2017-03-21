/* This class implements the camera to be used in autonomous via the IP Camera. 
 */
package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public abstract class Autonomous {
	private DriveTrain drive;
	private PoseManager poseManager;
	private Pose destinationPose;	
    protected PoseDelta delta;
    private NetworkTable table;
    private Gear gear;
	
    protected CameraData cameraData;
	
	public Autonomous(DriveTrain drive, PoseManager poseManager, Gear gear) {
		this.gear = gear;
		this.drive = drive;
		this.poseManager = poseManager;
		this.cameraData = new CameraData();
		this.table = NetworkTable.getTable("GRIP/myContoursReport");
		init();
	}
	
	public void update() {
		delta = destinationPose.subtract(poseManager.getCurrentPose());
		boolean updated = updateCameraData();
		periodic(drive.driveByPoseDelta(delta), updated);
		drive.shiftDriveHigh(true);
		poseManager.printDistance();		
		if(updated) {
			cameraData = getCameraDataValues();
		}
		
		// TODO: Store robot pose history here
		// TODO: Store camera data in this class, update here.
	}
	
	protected void updateDestination(PoseDelta delta) {
		destinationPose = poseManager.getCurrentPose().add(delta);
	}
	
	//returns new camera data values including angle and distance
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
		System.out.println("stopRobot method called");
		drive.driveTank(0, 0);
	}
	
	protected void dropGear() {
		gear.extendDropper();
	}
	
	
	protected abstract void init();
	protected abstract void periodic(boolean atDestination, boolean newCameraData);
	
	
}
