package org.usfirst.frc.team1700.robot;


public class BaselineAutonomous extends Autonomous {
	
double deadline;
    
    private enum AutoStage{
    	DRIVE,
    	DONE;
    }
    
    AutoStage currentAutoStage;

	public BaselineAutonomous(DriveTrain drive, PoseManager poseManager, Gear gear) {
		super(drive, poseManager, gear);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		updateCameraData();
		currentAutoStage = AutoStage.DRIVE;
		updateDestination(new PoseDelta(0,Constants.Values.Auto.FIRST_DISTANCE));
	}

	@Override
	protected void periodic(boolean atDestination, boolean newCameraData) {
		switch(currentAutoStage) {
		
		case DRIVE:
			System.out.println("Turning to face middle of field");
			if (atDestination) {
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

