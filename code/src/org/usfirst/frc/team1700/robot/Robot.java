
package org.usfirst.frc.team1700.robot;



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
    Vision vision; 
    Intake intake;
    PoseManager poseManager;
    Autonomous auto;
    DigitalInput firstAutoSwitch;
    DigitalInput secondAutoSwitch;
    CameraData cameraData;
    NetworkTable table;
    double targetAngle;
    
    
    public Robot() {
    	drive = new DriveTrain();
    	vision = new Vision();
        gear = new Gear();
        lowGoal = new LowGoal();
        intake = new Intake();
        poseManager = new PoseManager();
        leftDriveJoystick = new Joystick(Constants.DriverStation.LEFT_JOYSTICK.getPort());
        rightDriveJoystick = new Joystick(Constants.DriverStation.RIGHT_JOYSTICK.getPort());
        firstAutoSwitch = new DigitalInput(Constants.DigitalIO.FIRST_AUTO_SWITCH.getPort());
        secondAutoSwitch = new DigitalInput(Constants.DigitalIO.SECOND_AUTO_SWITCH.getPort());
        cameraData = new CameraData();
        table = NetworkTable.getTable("GRIP/myContoursReport");
        targetAngle = 0;
    }
	
    public void robotInit() {
		vision.initVision();
		drive.setDriveVoltageRampRates();
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
    	} else {
    		driveState();
    	}
    }
    
    protected boolean updateCameraData() {
		CameraData newCameraData = getCameraDataValues();
		if(newCameraData.timestamp > cameraData.timestamp) {
			cameraData = newCameraData;
			return true;
		}
		return false;
}
    
	protected CameraData getCameraDataValues() {
		double angle = table.getNumber("Angle", 0);
		long timestamp = (long) table.getNumber("Time", 0);
		double distance = table.getNumber("Distance", 0);
		return new CameraData(timestamp, angle, distance);
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
			targetAngle = poseManager.getCurrentPose().angle + cameraData.angle;
			
		}
		drive.driveTankByAngle(rightDriveJoystick.getRawAxis(1), targetAngle - poseManager.getCurrentPose().angle);
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
    
    public void driveState() {
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	drive.shiftDriveHigh(true);
		intake.runIntake();
		lowGoal.moveDown();
		gear.flapGearIntakePosition();
		gear.retractSlot();
    }
    
}
