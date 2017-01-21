package org.usfirst.frc.team1700.robot;
/**
	@author christine
	The RobotMap should be usedfor naming all ports, ids, etc. 
	so we can change them more easily and ensure that we are not reusing them.
	*/
public class Constants {
	
	//Don't wan't people to instantiate
	private Constants() {} 
	
	//ID's for Motor Controllers
	public static final int DRIVE_RIGHT_FRONT = 1,
							DRIVE_RIGHT_BACK = 2,
							DRIVE_LEFT_FRONT = 3,
							DRIVE_LEFT_BACK = 4;
	
	//Servo ID's
	public static final int GEAR_SERVO_ID = 0,
							LEFT_DRIVE_SERVO_ID = 1,
							RIGHT_DRIVE_SERVO_ID = 2;
	
	//Servo angle amounts
	public static final double GEAR_SERVO_OPEN_POSITION = 1.0,
							GEAR_SERVO_CLOSED_POSITION = 0.0,
							DRIVE_SERVO_SHIFT_HIGH_POSITION = 1.0,
							DRIVE_SERVO_SHIFT_LOW_POSITION = 0.0;
	
	//PDP ID
	public static final int PDP_ID = 5;
	
	//Joystick ports
	public static final int LEFT_JOYSTICK = 0,
							RIGHT_JOYSTICK = 1;
	
	//Buttons
	public static final int SHIFT_LOW = 1,
							SHIFT_HIGH = 2,
							RAMP_UP = 3,
							RAMP_DOWN = 4,
							FLAP_UP = 5,
							FLAP_DOWN = 6;
	
	//Vision constants
	public static final double TURNING_ANGLE_PROPORTION = 0.01;
	public static final double TARGET_ANGLE_TOLERANCE = 5;
	
}
