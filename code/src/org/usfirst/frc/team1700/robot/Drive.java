package org.usfirst.frc.team1700.robot;



import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;

public class Drive {
	CANTalon rightFront;
    CANTalon rightBack;
    CANTalon leftFront;
    CANTalon leftBack;
    Servo shiftingServo;
    PowerDistributionPanel pdp;
    Vision vision;
    private static double ANGLE_CONSTANT;
    private static double ANGLE_RANGE;
	
	public Drive() {
		vision = new Vision();
		rightFront = new CANTalon(Constants.DRIVE_RIGHT_FRONT); //TODO: Change ID's
		rightBack = new CANTalon(Constants.DRIVE_RIGHT_BACK);
		leftFront = new CANTalon(Constants.DRIVE_LEFT_FRONT);
		leftBack = new CANTalon(Constants.DRIVE_LEFT_BACK);
		pdp = new PowerDistributionPanel(Constants.PDP_ID);
		ANGLE_CONSTANT = 2;
		ANGLE_RANGE = 0.05;
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(rightSpeed);
		rightBack.set(rightSpeed);
	}
	
	public void shiftDrive(Joystick joy){
		if (joy.getRawButton(Constants.SHIFT_LOW)){
			shiftingServo.set(0.0);
		} else if (joy.getRawButton(Constants.SHIFT_HIGH)){
			shiftingServo.set(1.0);
		}
	}
	
	public void turnToAngle(double angle) {
		if (angle > ANGLE_RANGE) {
			driveTank(0.05, angle/ANGLE_CONSTANT);
		} else if (angle < ANGLE_RANGE) {
			driveTank(-angle/ANGLE_CONSTANT, 0.05);
		}
	}
	
}
