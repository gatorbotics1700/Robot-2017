package org.usfirst.frc.team1700.robot;


public class MiddlePegAutonomousWithoutVision extends Autonomous {
	
double deadline;
    
    private enum AutoStage{
    	SCORE,
    	WAIT,
    	RETRY,
    	DONE;
    }
    
    AutoStage currentAutoStage;
	double turnAngle;

	public MiddlePegAutonomousWithoutVision(DriveTrain drive, PoseManager poseManager) {
		super(drive, poseManager);
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
				currentAutoStage = AutoStage.DONE;
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

