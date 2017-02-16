
package org.usfirst.frc.team1700.robot;



import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


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
        
    }
	
    public void robotInit() {
//		vision.initVision();
		drive.setDriveVoltageRampRates();
    }
    

    public void autonomousInit() {
    	if(firstAutoSwitch.get() && secondAutoSwitch.get()) {
    		auto = new RightPegAutonomous(drive, poseManager);
    	} else if(firstAutoSwitch.get() && !secondAutoSwitch.get()) {
    		auto = new LeftPegAutonomous(drive, poseManager);
    	} else {
    		auto = new MiddlePegAutonomous(drive, poseManager);
    	}
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
    	drive.driveTank(leftDriveJoystick.getRawAxis(1), rightDriveJoystick.getRawAxis(1));
    	if(leftDriveJoystick.getRawButton(Constants.JoystickButtons.Left.GEAR_INTAKE.getId())) {
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
    
    public void gearState() {
    	drive.shiftDriveHigh(true);
		lowGoal.moveDown();
		intake.runIntake();
		gear.flapGearIntakePosition();
		gear.extendSlot();
    }
    
    public void ballIntakeState() {
    	drive.shiftDriveHigh(true);
		lowGoal.moveDown();
		intake.runIntake();
		gear.flapBallIntakePosition();
		gear.retractSlot();
    }
    
    public void climbState() {
    	drive.shiftDriveHigh(false);
		intake.climbIntake();
		lowGoal.moveDown();
		gear.flapGearIntakePosition();
		gear.retractSlot();
    }
    
    public void lowGoalState() {
    	drive.shiftDriveHigh(true);
		intake.lowGoalIntake();
		lowGoal.moveUp();
		gear.flapLowGoalPosition();
		gear.retractSlot();
    }
    
    public void driveState() {
    	drive.shiftDriveHigh(true);
		intake.runIntake();
		lowGoal.moveDown();
		gear.flapGearIntakePosition();
		gear.retractSlot();
    }
    
}
