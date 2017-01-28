
package org.usfirst.frc.team1700.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
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
    
    public Robot() {
    	drive = new Drive();
    	vision = new Vision();
        gear = new Gear();
        shooter = new LowGoal();
        intake = new Intake();
        leftDriveJoystick = new Joystick(Constants.LEFT_JOYSTICK);
        rightDriveJoystick = new Joystick(Constants.RIGHT_JOYSTICK);
        
    }
	

    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("Left Peg Auto", leftPeg);
        chooser.addObject("Right Peg Auto", rightPeg);
        chooser.addObject("Middle Peg Auto", middlePeg);
        pdp = new PowerDistributionPanel(1); //put ID in parentheses
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
    }
 
   
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
    		
    		
             break;
    	case defaultAuto:
    	default:
    		
            break;
    	}
    }

    @Override
    public void teleopInit() {
        vision.initVision();
    }
    
     
    
    public void teleopPeriodic() {
    	drive.update(0.0,0.0,true);
    	if (leftDriveJoystick.getRawButton(1)) {
    		double angle = 0;
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
