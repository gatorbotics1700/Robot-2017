package org.usfirst.frc.team1700.robot;

import org.usfirst.frc.team1700.robot.Robot.AutoStage;

public class MiddlePegAutonomous extends Autonomous {
	
double deadline;
    
    private enum AutoStage{
    	SCORE,
    	WAIT,
    	RETRY,
    	DONE;
    }
    
    AutoStage currentAutoStage;
	double turnAngle;

	public MiddlePegAutonomous(DriveTrain drive, PoseManager poseManager, Gear gear) {
		super(drive, poseManager, gear);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		super.updateCameraData();
		currentAutoStage = AutoStage.SCORE;
		updateDestination(new PoseDelta(0,Constants.Values.Auto.MIDDLE_PEG_DISTANCE));
		
	}

	@Override
	protected void periodic(boolean atDestination, boolean newCameraData) {
		switch(currentAutoStage) {
		case SCORE:
			if(newCameraData) {
				super.updateDestination(new PoseDelta(cameraData.angle, delta.distanceDelta));
	//			Pose pastPose = poseManager.poseHistory.getHistory(System.currentTimeMillis()-cameraData.timestamp);
	//			double newDistance = Math.sqrt(Math.pow(pastPose.distance,2)+Math.pow(pastPose.distance,2)-2*pastPose.distance*pastPose.distance*Math.cos(pastPose.angle));
	//			double insideAngle = Math.asin(Math.sin(pastPose.angle)/newDistance*pastPose.distance);
	//			double newAngle = Constants.radiansToDegrees(Math.PI - insideAngle);
	//			Pose newPose = new Pose(newAngle,Constants.Values.Auto.SECOND_DISTANCE);
			}
			if(super.drive.driveByPoseDelta(delta)) {
				currentAutoStage = AutoStage.WAIT;
	        	deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
			}
			break;
		case WAIT:
			drive.driveTank(0, 0);
			if(System.currentTimeMillis() > deadline) {
				if(gear.gearInSlot()) {
	        		updateDestination(new PoseDelta(0, -Constants.Values.Auto.BACK_UP_DISTANCE));
					currentAutoStage = AutoStage.RETRY;
				} else {
					currentAutoStage = AutoStage.DONE;
				}
			}
			break;
		case RETRY:
			if(drive.driveByPoseDelta(delta)) {
				updateDestination(new PoseDelta(0, Constants.Values.Auto.BACK_UP_DISTANCE));
	    		currentAutoStage = AutoStage.SCORE;
			}
			break;
		case DONE:
			drive.driveTank(0, 0);
			break;
		}
			
	}
		
}

