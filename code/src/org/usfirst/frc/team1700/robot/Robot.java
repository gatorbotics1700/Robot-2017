
package org.usfirst.frc.team1700.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    final String leftPeg = "Left Peg Auto";
    final String rightPeg = "Right Peg Auto";
    final String middlePeg = "Middle Peg Auto";
    String autoSelected;
    SendableChooser chooser;
    Joystick leftDriveJoystick;
    Joystick rightDriveJoystick;
    Joystick operatorJoystick;
    PowerDistributionPanel pdp;
    DriveTrain drive;
    Gear gear;
    LowGoal lowGoal;
    Vision vision; 
    Intake intake;
    PoseManager poseManager;
    Pose destinationPose;
    CameraData cameraData;
    NetworkTable table;
    double deadline;
    
    public enum AutoStage{
    	DRIVE_FORWARD,
    	TURN,
    	SCORE,
    	WAIT,
    	RETRY,
    	DONE;
    }
    
    AutoStage currentAutoStage;
    
    public Robot() {
    	drive = new DriveTrain();
    	vision = new Vision();
        gear = new Gear();
        lowGoal = new LowGoal();
        intake = new Intake();
        poseManager = new PoseManager();
        cameraData = new CameraData();
		table = NetworkTable.getTable("GRIP/myContoursReport");
        destinationPose = null;
        leftDriveJoystick = new Joystick(Constants.DriverStation.LEFT_JOYSTICK.getPort());
        rightDriveJoystick = new Joystick(Constants.DriverStation.RIGHT_JOYSTICK.getPort());
        
    }
	
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("Left Peg Auto", leftPeg);
        chooser.addObject("Right Peg Auto", rightPeg);
        chooser.addObject("Middle Peg Auto", middlePeg);
        SmartDashboard.putData("Auto choices", chooser);
    	System.out.println("Running robot init");
//		vision.initVision();
		drive.setDriveVoltageRampRates();
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		currentAutoStage = AutoStage.DRIVE_FORWARD;
		destinationPose = poseManager.getCurrentPose().add(new PoseDelta(0,Constants.Values.Auto.FIRST_DISTANCE));
    }
 
   /**
    * Uses the PoseManager and PoseDelta classes to keep track of current positions and set destination poses
    * Score mode uses camera to align
    */
    public void autonomousPeriodic() {
    	poseManager.storeCurrentPose();
    	drive.shiftDriveHigh(true);
    	PoseDelta delta = destinationPose.subtract(poseManager.getCurrentPose());
    		switch (currentAutoStage) {
        	case DRIVE_FORWARD:
        		System.out.println("In drive forward.");
        		if(drive.driveByPoseDelta(delta)) {
        			System.out.println("Stopped driving straight");
        			destinationPose = poseManager.getCurrentPose().add(new PoseDelta(Constants.Values.Auto.TURN_ANGLE,0));
        			System.out.println("Angle while driving straight: " + destinationPose.angle);
        			currentAutoStage = AutoStage.TURN;
        		}
        		//drive.driveByPoseDelta(delta);
    			break;
        	case TURN:
        		System.out.println("In turn.");
        		if (drive.driveByPoseDelta(delta)) {
        			System.out.println("stopped turning");
        			destinationPose = poseManager.getCurrentPose().add(new PoseDelta(0,Constants.Values.Auto.SECOND_DISTANCE));
        			System.out.println("Angle in turn modet: " + destinationPose.angle);
        			currentAutoStage = AutoStage.SCORE;
        			cameraData.timestamp = System.currentTimeMillis();
        			cameraData.angle = 0;
        		}
        		//drive.driveByPoseDelta(delta);
        		break;
        	case SCORE:
//        		System.out.println("in score.");
        		if(updateCameraData()) {
        			destinationPose = poseManager.getCurrentPose().add(new PoseDelta(cameraData.angle, delta.distanceDelta));
//        			Pose pastPose = poseManager.poseHistory.getHistory(System.currentTimeMillis()-cameraData.timestamp);
//        			double newDistance = Math.sqrt(Math.pow(pastPose.distance,2)+Math.pow(pastPose.distance,2)-2*pastPose.distance*pastPose.distance*Math.cos(pastPose.angle));
//        			double insideAngle = Math.asin(Math.sin(pastPose.angle)/newDistance*pastPose.distance);
//        			double newAngle = Constants.radiansToDegrees(Math.PI - insideAngle);
//        			Pose newPose = new Pose(newAngle,Constants.Values.Auto.SECOND_DISTANCE);
        			delta = destinationPose.subtract(poseManager.getCurrentPose());
        		}
        		if(drive.driveByPoseDelta(delta)) {
        			currentAutoStage = AutoStage.WAIT;
                	deadline = Constants.Values.Auto.WAIT_TIME + System.currentTimeMillis();
        		}
        		System.out.println("Angle in score mode: " + destinationPose.angle);
        		break;
        	case WAIT:
        		drive.driveTank(0, 0);
    			if(System.currentTimeMillis() > deadline) {
    				if(gear.gearInSlot()) {
    	        		destinationPose = poseManager.getCurrentPose().add(new PoseDelta(0, -Constants.Values.Auto.BACK_UP_DISTANCE));
    					currentAutoStage = AutoStage.RETRY;
    				} else {
    					currentAutoStage = AutoStage.DONE;
    				}
    			}
    			break;
        	case RETRY:
        		if(drive.driveByPoseDelta(delta)) {
        			destinationPose = poseManager.getCurrentPose().add(new PoseDelta(0, Constants.Values.Auto.BACK_UP_DISTANCE));
            		currentAutoStage = AutoStage.SCORE;
        		}
        		break;
        	case DONE:
        		drive.driveTank(0, 0);
        		break;
        	}
    		
    }


    @Override
    public void teleopInit() {
        destinationPose = null;
        vision.initVision();
    }
    
    public CameraData getCameraDataValues() {
		double angle = table.getNumber("Angle", 0);
		long timestamp = (long) table.getNumber("Time", 0);
		double distance = table.getNumber("Distance", 0);
		return new CameraData(timestamp, angle, distance);
}
	
	 //Updates values if the values are new

	public boolean updateCameraData() {
			CameraData newCameraData = getCameraDataValues();
			if(newCameraData.timestamp > cameraData.timestamp) {
				cameraData = newCameraData;
				return true;
			}
			return false;
	}
    
    /**
     * Buttons handle all of the subsystems on the robot.
     * The default (aka when a button is not pressed) is when the the driver is moving the robot around the field
     */
    
    public void teleopPeriodic() {
    	//updateCameraData();
    	//poseManager.getCurrentPose();
    	System.out.println("Ramp Voltage: " + lowGoal.getRampHeight());
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	
    	if(leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.GEAR_INTAKE.getId())) {
    		drive.shiftDriveHigh(true);
    		lowGoal.moveDown();
    		intake.runIntake();
    		gear.flapGearIntakePosition();
    		gear.extendSlot();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.BALL_INTAKE.getId())) {
    		drive.shiftDriveHigh(true);
    		lowGoal.moveDown();
    		intake.runIntake();
    		gear.flapBallIntakePosition();
    		gear.retractSlot();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.CLIMB.getId())) {
    		drive.shiftDriveHigh(false);
    		intake.climbIntake();
    		lowGoal.moveDown();
    		gear.flapGearIntakePosition();
    		gear.retractSlot();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.LOW_GOAL_SCORE.getId())) {
    		drive.shiftDriveHigh(true);
    		intake.lowGoalIntake();
    		lowGoal.moveUp();
    		gear.flapLowGoalPosition();
    		gear.retractSlot();
    	} else {
    		drive.shiftDriveHigh(true);
    		intake.runIntake(); //test
    		lowGoal.moveDown();
    		gear.flapGearIntakePosition();
    		gear.retractSlot();
    	}
    }
    
    

    public boolean drivePose() {
    	if (destinationPose == null) {
    		drive.driveTank(
    				leftDriveJoystick.getRawAxis(1),
    				rightDriveJoystick.getRawAxis(1));
    		return true;
    	} else {
    		Pose currentPose = poseManager.getCurrentPose();
    		PoseDelta driveDelta = destinationPose.subtract(currentPose);
    		System.out.println("Pose: " + destinationPose);
    		System.out.println("Pose Delta: " + driveDelta);
    		System.out.println("Current pose: " + currentPose);
    		if (drive.driveByPoseDelta(driveDelta)) {
    			System.out.println("Done driving to pose.");
    			return true;
    		}
    		return false;
    	}
    }

    
    
    public void testPeriodic() {
    
    }
    
}
