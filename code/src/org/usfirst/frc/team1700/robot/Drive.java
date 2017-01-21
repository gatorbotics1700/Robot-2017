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
	
	public Drive() {
		vision = new Vision();
		rightFront = new CANTalon(0); //TODO: Change ID's
		rightBack = new CANTalon(1);
		leftFront = new CANTalon(2);
		leftBack = new CANTalon(3);
		pdp = new PowerDistributionPanel(4);
		ANGLE_CONSTANT = 0.01;
	}
	
	public void driveTank(double leftSpeed, double rightSpeed) {
		leftFront.set(leftSpeed);
		leftBack.set(leftSpeed);
		rightFront.set(rightSpeed);
		rightBack.set(rightSpeed);
	}
	
	public void shiftDrive(Joystick joy){
		if (joy.getRawButton(1)){
			shiftingServo.set(0.0);
		} else if (joy.getRawButton(2)){
			shiftingServo.set(1.0);
		}
	}
	
	public void turnToAngle(double angle) {
		if(angle > 0) {
			driveTank(0.1, angle/ANGLE_CONSTANT);
		} else if (angle < 0) {
			driveTank(-angle/ANGLE_CONSTANT, 0.1);
		}
	}
	
}
