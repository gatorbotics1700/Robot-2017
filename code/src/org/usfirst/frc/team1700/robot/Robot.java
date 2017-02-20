
package org.usfirst.frc.team1700.robot;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Robot extends IterativeRobot {
    Joystick leftDriveJoystick;
    Joystick rightDriveJoystick;    
    DriveTrain drive;
    Gear gear;
    LowGoal lowGoal;
    List<Vision> vision;
    Intake intake;
    PoseManager poseManager;
    Autonomous auto;
    DigitalInput firstAutoSwitch;
    DigitalInput secondAutoSwitch;
    CameraData cameraData;
    NetworkTable table;
    Pose targetPose;
    
    
    public Robot() {
    	drive = new DriveTrain();
        gear = new Gear();
        lowGoal = new LowGoal();
        intake = new Intake();
        poseManager = new PoseManager();
        leftDriveJoystick = new Joystick(Constants.DriverStation.LEFT_JOYSTICK.getPort());
        rightDriveJoystick = new Joystick(Constants.DriverStation.RIGHT_JOYSTICK.getPort());
        firstAutoSwitch = new DigitalInput(Constants.DigitalIO.FIRST_AUTO_SWITCH.getPort());
        secondAutoSwitch = new DigitalInput(Constants.DigitalIO.SECOND_AUTO_SWITCH.getPort());
        cameraData = new CameraData();
        vision = Arrays.asList(new Vision(0), new Vision(1));
        table = NetworkTable.getTable("GRIP/myContoursReport");
        targetPose = new Pose(0,0);
    }
	
    public void robotInit() {
    	drive.setDriveVoltageRampRates();
    	vision.get(0).turnOnVision();
    	vision.get(1).turnOnVision();
    }
    

    public void autonomousInit() {
    	auto = new MiddlePegAutonomous(drive, poseManager);
//    	if(firstAutoSwitch.get() && secondAutoSwitch.get()) {
//    		auto = new RightPegAutonomous(drive, poseManager);
//    	} else if(firstAutoSwitch.get() && !secondAutoSwitch.get()) {
//    		auto = new LeftPegAutonomous(drive, poseManager);
//    	} else {
//    		auto = new MiddlePegAutonomous(drive, poseManager);
//    	}
    	auto.init();
    }
 
   
    public void autonomousPeriodic() {
    	auto.update(gear.gearInSlot());
    	driveState();
    	
    }


    @Override
    public void teleopInit() {
        targetPose = poseManager.getCurrentPose();
    }
    
    
    /**
     * The default (aka when a button is not pressed) is when the the driver is moving the robot around the field
     */
    
    public void teleopPeriodic() {
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.VISION.getId())) {
    		visionState();
    	} else if(leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.GEAR_INTAKE.getId())) {
    		gearState();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.BALL_INTAKE.getId())) {
    		ballIntakeState();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.CLIMB.getId())) {
    		climbState();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.LOW_GOAL_SCORE.getId())) {
    		lowGoalState();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.RESET.getId())) {
    		resetState();
    	} else if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.ANGLE.getId())) {
    		driveToAngle();
    	} else if (rightDriveJoystick.getRawButton(Constants.JoystickButtons.Right.SERVOS.getId())) {
    		drive.shiftDrive();
    	} else {
    		driveState();
    	}
    }
    
    protected boolean updateCameraData() {
    	//check if there's two. If two call stereoimaging if one call account for camera offset
		List<CameraData> newCameraData = getCameraDataValues();
			if(newCameraData.get(0).timestamp > cameraData.timestamp || newCameraData.get(1).timestamp > cameraData.timestamp) {
				cameraData = Vision.stereoImaging(newCameraData.get(0), newCameraData.get(1), Constants.Values.Vision.CAMERA_LEFT_OFFSET, Constants.Values.Vision.CAMERA_RIGHT_OFFSET);;
				System.out.println("Camera Data! " + cameraData);
				return true;
			}
		return false;
    }
    
	protected List<CameraData> getCameraDataValues() {
		List<CameraData> newCameraData = new ArrayList<CameraData>();
		for(int i=0; i<vision.size(); i++) {
			double angle = table.getNumber(i+":Angle", 0);
			long timestamp = (long) table.getNumber(i+":Time", 0);
			double distance = table.getNumber(i+":Distance", 0);
			newCameraData.add(new CameraData(timestamp, angle, distance));
		}
		return newCameraData;
	}
    
    public void gearState() {
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	drive.shiftDriveHigh(true);
		lowGoal.moveDown();
		intake.runIntake();
		gear.flapGearIntakePosition();
		gear.extendSlot();
		
    }
    
    public void visionState() {
    	drive.shiftDriveHigh(true);
		lowGoal.moveDown();
		intake.runIntake();
		gear.flapGearIntakePosition();
		gear.extendSlot();
		if (updateCameraData()) {
			targetPose = poseManager.getCurrentPose().add(new PoseDelta(Constants.radiansToDegrees(cameraData.angle), cameraData.distance)) ;
			
		}
		//drive.driveByPoseDelta(targetPose.subtract(poseManager.getCurrentPose()));
    }
    
    public void resetState() {
    	targetPose = poseManager.getCurrentPose();
    }
    
    public void ballIntakeState() {
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	drive.shiftDriveHigh(true);
		lowGoal.moveDown();
		intake.runIntake();
		gear.flapBallIntakePosition();
		gear.retractSlot();
    }
    
    public void climbState() {
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	drive.shiftDriveHigh(false);
		intake.climbIntake();
		lowGoal.moveDown();
		gear.flapGearIntakePosition();
		gear.retractSlot();
    }
    
    public void lowGoalState() {
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	drive.shiftDriveHigh(true);
		intake.lowGoalIntake();
		lowGoal.moveUp();
		gear.flapLowGoalPosition();
		gear.retractSlot();
    }
    
    public void driveToAngle() {
    	if (updateCameraData()) {
			targetPose = poseManager.getCurrentPose().add(new PoseDelta(Constants.radiansToDegrees(cameraData.angle), 0));
		}
		drive.driveByPoseDeltaLowGear(targetPose.subtract(poseManager.getCurrentPose()));
    }
    
    public void driveState() {
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	drive.shiftDriveHigh(true);
		intake.runIntake();
		lowGoal.moveDown();
		gear.flapGearIntakePosition();
		gear.retractSlot();
    }
    
}
