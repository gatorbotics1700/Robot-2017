package org.usfirst.frc.team1700.robot;


public class MiddlePegAutonomousWithoutVision extends Autonomous {
	
double deadline;
    
    private enum AutoStage{
    	SCORE,
    	WAIT,
    	BACK_UP,
    	TURN,
    	DRIVE_TO_SIDE,
    	TURN_BACK,
    	CROSS_FIELD,
    	DONE;
    }
    
    AutoStage currentAutoStage;
	double turnAngle;

	public MiddlePegAutonomousWithoutVision(DriveTrain drive, PoseManager poseManager, Gear gear) {
		super(drive, poseManager, gear);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		updateCameraData();
		currentAutoStage = AutoStage.SCORE;
		updateDestination(new PoseDelta(0,Constants.Values.Auto.MIDDLE_PEG_DISTANCE));
		
	}

	@Override
	protected void periodic(boolean atDestination, boolean newCameraData) {
		switch(currentAutoStage) {
		case SCORE:
			if(atDestination) {
	        	deadline = Constants.Values.Auto.DEPLOY_TIME + System.currentTimeMillis();
	        	updateDestination(new PoseDelta(0,0));
				currentAutoStage = AutoStage.WAIT;
			}
			break;			
		case WAIT:
			updateDestination(new PoseDelta(0,0));
			dropGear();
			if(System.currentTimeMillis() > deadline) {
				updateDestination(new PoseDelta(0, -Constants.Values.Auto.BACK_UP_DISTANCE));
				currentAutoStage = AutoStage.BACK_UP;
			}
			break;
		case BACK_UP:
    		System.out.println("Driving back");
    		if (atDestination) {
    			System.out.println("Stop back");
				updateDestination(new PoseDelta(Constants.Values.Auto.TURN_MIDDLE_ANGLE, 0));
            	currentAutoStage = AutoStage.DONE;
    		}
    		break;
		case TURN:
			System.out.println("Driving to side");
			if (atDestination) {
				updateDestination(new PoseDelta(0, Constants.Values.Auto.TO_SIDE_DISTANCE));
				currentAutoStage = AutoStage.DRIVE_TO_SIDE;
			}
			break;
		case DRIVE_TO_SIDE:
			System.out.println("Turning to face middle of field");
			if (atDestination) {
				updateDestination(new PoseDelta(-Constants.Values.Auto.TURN_MIDDLE_ANGLE, 0));
				currentAutoStage = AutoStage.TURN_BACK;
			}
			break;
    	case TURN_BACK:
    		System.out.println("Turning back");
    		if (atDestination) {
    			System.out.println("stopped turning");
				updateDestination(new PoseDelta(0, Constants.Values.Auto.CROSS_DISTANCE));
            	currentAutoStage = AutoStage.CROSS_FIELD;
    		}
    		break;
    	case CROSS_FIELD:
    		if (atDestination) {
    			System.out.println("stopped turning");
				updateDestination(new PoseDelta(0, 0));
				currentAutoStage = AutoStage.DONE;
    		}
    		break;
    	case DONE:
			updateDestination(new PoseDelta(0, 0));
    		System.out.println("Done");
    		stopRobot();
    		break;
		}
			
	}
		
}

