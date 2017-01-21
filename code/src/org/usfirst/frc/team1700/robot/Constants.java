package org.usfirst.frc.team1700.robot;
/**
	@author christine
	The RobotMap should be usedfor naming all ports, ids, etc. 
	so we can change them more easily and ensure that we are not reusing them.
	*/
public class Constants {
	
	//Don't wan't people to instantiate
	private Constants() {
		double circumference = WHEEL_DIAMETER_INCHES*Math.PI;
		TICKS_PER_INCH = ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION / circumference;
	} 
	
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
	public static final double TURNING_ANGLE_PROPORTION = 0.01,
							   DRIVING_DISTANCE_PROPORTION = (1.0/48.0),
							   TARGET_ANGLE_TOLERANCE = 5;
	
	//Quadrature encoders
	//TO-DO: Code based on color of wires. 
	public static final int QUAD_ENCODER_LEFT_1 = 5,
							QUAD_ENCODER_LEFT_2 = 6,
							QUAD_ENCODER_RIGHT_1 = 7,
							QUAD_ENCODER_RIGHT_2 = 8;
	
	//TO-DO: Change these values.
	public static final double WHEEL_DIAMETER_INCHES = 6,
							   DRIVE_GEAR_REDUCTION = 100,
							   ENCODER_TICKS_PER_REV = 250;
	
	private static double TICKS_PER_INCH;
							
	public double getTicksPerInch() {
		return TICKS_PER_INCH;
	}
	
	public static double ticksToInches(double ticks) {
		return ticks / TICKS_PER_INCH;
	}
	
	public static double inchesToTicks(double inches) {
		return inches * TICKS_PER_INCH; 
	}
	
}
