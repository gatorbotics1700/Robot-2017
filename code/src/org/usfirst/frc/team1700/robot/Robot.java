
package org.usfirst.frc.team1700.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
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
    
    public Robot() {
    	drive = new Drive();
    	vision = new Vision();
        gear = new Gear();
        shooter = new LowGoal();
        intake = new Intake();
        poseManager = new PoseManager();
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
    	drive.zeroEncoders();
    	System.out.println("Running robot init");

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
		drive.resetNavX();
		vision.initVision();
    }
 
   
    public void autonomousPeriodic() {
    	drive.setTargetDistance(36.0);
    	double angle;
    	synchronized(vision.imgLock) {
    		angle = vision.angleDegrees;
    	}
    	drive.setTargetAngleDelta(angle);
    }

    @Override
    public void teleopInit() {
        
    }
    
     
    
    public void teleopPeriodic() {
    	drivePose();
    }
    
    public void drivePose() {
    	//if no pose, then basic driving, else pose driving
    	if (destinationPose == null) {
    		drive.driveTank(
    				leftDriveJoystick.getAxis(AxisType.kY),
    				rightDriveJoystick.getAxis(AxisType.kY));
    	} else {
    		Pose currentPose = poseManager.getCurrentPose();
    		PoseDelta driveDelta = destinationPose.subtract(currentPose);
    		if (drive.driveByPoseDelta(driveDelta)) {
    			destinationPose = null;
    		}
    	}
    	
    	//Pose Testing, pose is (angle, distance)
    	if (leftDriveJoystick.getRawButton(0)) {
    		PoseDelta delta = new PoseDelta(90, 0);
    		destinationPose = poseManager.getCurrentPose().add(delta);
    	}
    	
    	//Shifting
    	if (rightDriveJoystick.getRawButton(Constants.SHIFT_HIGH_DRIVE)) {
    		drive.shiftDrive(true);
    	} else if (rightDriveJoystick.getRawButton(Constants.SHIFT_LOW_DRIVE)) {
    		drive.shiftDrive(false);
    	}
    }
    
    @Deprecated
    public void driveNoPose() {
    	drive.update(0.0,0.0,true);
    	if (leftDriveJoystick.getRawButton(1)) {
    		double angle;
    		synchronized(vision.imgLock) {
    			angle = vision.angleDegrees;
    		}
		drive.setTargetAngleDelta(angle);
    	} else if(leftDriveJoystick.getRawButton(2)) {
    		drive.NavX.reset();
        	drive.setTargetDistance(50.0);
    	}
    }
    
    
    public void testPeriodic() {
    
    }
    
}
