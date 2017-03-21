package org.usfirst.frc.team1700.robot;

public class SidePegAutonomous extends Autonomous {

	double deadline;
    
    private enum AutoStage{
    	DRIVE_FORWARD,
    	TURN,
    	SCORE,
    	WAIT,
    	RETRY,
    	DONE;
    }
    
    AutoStage currentAutoStage;
	double turnAngle;
	
	SidePegAutonomous(DriveTrain drive, PoseManager poseManager, Gear gear) {
		super(drive, poseManager, gear);
	}
	
	@Override
	protected void init() {
		super.updateCameraData();
		currentAutoStage = AutoStage.DRIVE_FORWARD;
		updateDestination(new PoseDelta(0,Constants.Values.Auto.FIRST_DISTANCE));
	}

	@Override
	protected void periodic(boolean atDestination, boolean newCameraData) {
    		switch (currentAutoStage) {
        	case DRIVE_FORWARD:
        		System.out.println("In drive forward.");
        		if(atDestination) {
        			System.out.println("Stopped driving straight");
        			updateDestination(new PoseDelta(turnAngle, 0));
        			currentAutoStage = AutoStage.TURN;
        		}
    			break;
        	case TURN:
        		System.out.println("In turn.");
        		if (atDestination) {
        			System.out.println("stopped turning");
        			updateDestination(new PoseDelta(0,Constants.Values.Auto.SECOND_DISTANCE));
        			currentAutoStage = AutoStage.SCORE;
        			resetCameraData();
        		}
        		//drive.driveByPoseDelta(delta);
        		break;
        	case SCORE:
//        		System.out.println("in score.");
        		if(newCameraData) {
        			super.updateDestination(new PoseDelta(cameraData.angle, delta.distanceDelta));
//        			Pose pastPose = poseManager.poseHistory.getHistory(System.currentTimeMillis()-cameraData.timestamp);
//        			double newDistance = Math.sqrt(Math.pow(pastPose.distance,2)+Math.pow(pastPose.distance,2)-2*pastPose.distance*pastPose.distance*Math.cos(pastPose.angle));
//        			double insideAngle = Math.asin(Math.sin(pastPose.angle)/newDistance*pastPose.distance);
//        			double newAngle = Constants.radiansToDegrees(Math.PI - insideAngle);
//        			Pose newPose = new Pose(newAngle,Constants.Values.Auto.SECOND_DISTANCE);
        		}
        		if(atDestination) {
        			currentAutoStage = AutoStage.WAIT;
                	deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
        		}
        		break;
        	case WAIT:
        		stopRobot();
    			if(System.currentTimeMillis() > deadline) {
					currentAutoStage = AutoStage.DONE;
    			}
    			break;
        	case RETRY:
        		if(atDestination) {
        			updateDestination(new PoseDelta(0, Constants.Values.Auto.BACK_UP_DISTANCE));
            		currentAutoStage = AutoStage.SCORE;
        		}
        		break;
        	case DONE:
        		stopRobot();
        		break;
        	}
	}

}
