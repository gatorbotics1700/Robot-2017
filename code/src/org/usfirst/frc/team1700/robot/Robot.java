
package org.usfirst.frc.team1700.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    Joystick leftDriveJoystick;
    Joystick rightDriveJoystick;
    Joystick operatorJoystick;
    PowerDistributionPanel pdp;
    Drive drive;
    
    
    public Robot() {
    	drive = new Drive();
        
        leftDriveJoystick = new Joystick(0);
        rightDriveJoystick = new Joystick(1);
        operatorJoystick = new Joystick(2);
    }
	

    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        pdp = new PowerDistributionPanel(1); //put ID in parentheses
        SmartDashboard.putData("Auto choices", chooser);
        
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
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    
    public void teleopPeriodic() {
        drive.driveTank(-leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
        drive.shiftDrive(leftDriveJoystick);
    }
    
    
    
    public void testPeriodic() {
    
    }
    
}
