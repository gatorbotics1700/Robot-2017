package org.usfirst.frc.team1700.robot;


public class SidePegAutonomousWithoutVision extends Autonomous {

	double deadline;
    
    private enum AutoStage{
    	DRIVE_FORWARD,
    	HOLD_DISTANCE,
    	TURN,
    	HOLD_TURN,
    	SCORE,
    	HOLD_SCORE,
    	BACK_UP,
    	TURN_BACK,
    	CROSS_FIELD,
    	DONE;
    }
    
    AutoStage currentAutoStage;
	double turnAngle;
	double targetDistance;
	
	SidePegAutonomousWithoutVision(DriveTrain drive, PoseManager poseManager) {
		super(drive, poseManager);
	}
	
	@Override
	protected void init() {
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
        			deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
                	currentAutoStage = AutoStage.HOLD_DISTANCE;
        		}
    			break;
        	case HOLD_DISTANCE:
        		System.out.println("Holding distance");
				if(System.currentTimeMillis() > deadline) {
        			updateDestination(new PoseDelta(turnAngle, 0));
					currentAutoStage = AutoStage.TURN;
				}
				break;
        	case TURN:
        		System.out.println("In turn.");
        		if (atDestination) {
        			System.out.println("stopped turning");
        			deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
                	currentAutoStage = AutoStage.HOLD_TURN;
        		}
        		//drive.driveByPoseDelta(delta);
        		break;
        	case HOLD_TURN:
        		System.out.println("Holding turn");
				if(System.currentTimeMillis() > deadline) {
	    			updateDestination(new PoseDelta(0,Constants.Values.Auto.SECOND_DISTANCE));
					currentAutoStage = AutoStage.SCORE;
				}
				break;
        	case SCORE:
        		System.out.println("In score");
        		if(atDestination) {
        			System.out.println("Stopped driving straight");
        			currentAutoStage = AutoStage.HOLD_SCORE;
        		}
        		break;
        	case HOLD_SCORE:
        		System.out.println("Holding score");
				if(System.currentTimeMillis() > deadline) {
					updateDestination(new PoseDelta(0, -Constants.Values.Auto.SECOND_DISTANCE));
					currentAutoStage = AutoStage.BACK_UP;
				}
    			break; 
        	case BACK_UP:
        		System.out.println("Driving back");
        		if (atDestination) {
        			System.out.println("Stop back");
        			deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
					updateDestination(new PoseDelta(-turnAngle, 0));
                	currentAutoStage = AutoStage.TURN_BACK;
        		}
        		break;
        	case TURN_BACK:
        		System.out.println("Turning back");
        		if (atDestination) {
        			System.out.println("stopped turning");
        			deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
					updateDestination(new PoseDelta(0, Constants.Values.Auto.CROSS_DISTANCE));
                	currentAutoStage = AutoStage.CROSS_FIELD;
        		}
        		break;
        	case CROSS_FIELD:
        		if (atDestination) {
        			System.out.println("stopped turning");
        			deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
					currentAutoStage = AutoStage.DONE;
        		}
        		break;
        	case DONE:
        		System.out.println("Done");
        		stopRobot();
        		break;
        	}
	}

}
