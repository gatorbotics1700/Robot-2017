
package org.usfirst.frc.team1700.robot;


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
    
    public Robot() {
    	drive = new DriveTrain();
    	//vision = new Vision();
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
		//vision.initVision();
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
    }
 
   
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
        destinationPose = null;
    }
    
     
    
    public void teleopPeriodic() {
    	updateCameraData();
    	if (drivePose()) {
    		destinationPose = null;
    	}
    	
    	if (leftDriveJoystick.getRawButton(1)) {
    		PoseDelta delta = new PoseDelta(90, 0);
    		destinationPose = poseManager.getCurrentPose().add(delta);
    	}
    	
    	if (rightDriveJoystick.getRawButton(1)) {
    		PoseDelta delta = new PoseDelta(0,12);
    		destinationPose = poseManager.getCurrentPose().add(delta);
    	}
    	
    	if (rightDriveJoystick.getRawButton(2)) {
    		PoseDelta delta = new PoseDelta(-poseManager.getCurrentPose().angle,-poseManager.getCurrentPose().distance);
    		destinationPose = poseManager.getCurrentPose().add(delta);
    	}
    	
    	// Shift high for driving
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.SHIFT_HIGH_DRIVE.getId())) {
    		drive.shiftDrive(true);
    	}
    	
    	// Shift low for driving
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.SHIFT_LOW_DRIVE.getId())) {
    		drive.shiftDrive(false);
    	}
    	
    	// Low goal: moves ramp up, stops front intake and back drives back intake, shifts gear flap
    	if(leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.DUMPER_UP.getId())){
    		lowGoal.moveUp();
    		intake.lowGoalIntake();
    		gear.lowGoalPosition();
    	}
    	
    	// Moves ramp down. To be used after low goal
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.DUMPER_DOWN.getId())) {
    		lowGoal.moveDown();
    	}
    	
    	// Shifts flap for ball intake
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.FLAP_BALL_INTAKE_POSITION.getId())) {
    		gear.ballIntakePosition();
    	}
    	
    	// Shifts flap for gear intake. May change to be the default position.
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.FLAP_GEAR_POSITION.getId())) {
    		gear.gearIntakePosition();
    	}
    	
    	// Shifts low for climbing. Powers intake motor to help climb.
    	if (leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.SHIFT_LOW_CLIMB.getId())) {
    		drive.shiftDrive(false);
    		intake.climbIntake();
    	}
    	
    	// Aligns robot to peg
    	if (rightDriveJoystick.getRawButton(Constants.JoystickButtons.Right.ALIGN_TO_PEG.getId())) {
    		// TODO:integrate vision code when it's finished
    	}
    	
    	if(leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.EXTEND_GEAR.getId())){
    		gear.extend();
    	} else{
    		gear.retract();
    	}
    	
    }
    
    public CameraData getCameraDataValues() {
    	double angle = table.getNumber("Angle", 0);
    	long timestamp = (long) table.getNumber("Time", 0);
    	return new CameraData(angle, timestamp);
    }
    
    public boolean updateCameraData() {
    	CameraData newCameraData = getCameraDataValues();
    	if(newCameraData.timestamp > cameraData.timestamp) {
    		cameraData = newCameraData;
    		return true;
    	}
    	return false;
    }
    
    public boolean drivePose() {
    	if (destinationPose == null) {
    		drive.driveTank(
    				leftDriveJoystick.getAxis(AxisType.kY),
    				rightDriveJoystick.getAxis(AxisType.kY));
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
