
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
    Drive drive;
    Gear gear;
    LowGoal shooter;
    Vision vision; 
    Intake intake;
    PoseManager poseManager;
    Pose destinationPose;
    CameraData cameraData;
    NetworkTable table;
    
    public Robot() {
    	drive = new Drive();
    	//vision = new Vision();
        gear = new Gear();
        shooter = new LowGoal();
        intake = new Intake();
        poseManager = new PoseManager();
        cameraData = new CameraData();
		table = NetworkTable.getTable("GRIP/myContoursReport");
        destinationPose = null;
        leftDriveJoystick = new Joystick(Constants.LEFT_JOYSTICK);
        rightDriveJoystick = new Joystick(Constants.RIGHT_JOYSTICK);
        
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
