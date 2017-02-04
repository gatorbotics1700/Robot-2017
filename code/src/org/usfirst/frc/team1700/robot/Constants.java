package org.usfirst.frc.team1700.robot;
/**
	@author christine
	The RobotMap should be used for naming all ports, ids, etc. 
	so we can change them more easily and ensure that we are not reusing them.
	*/
public class Constants {
	
	//Don't wan't people to instantiate
	private Constants() {

	} 
	
	// CANTalon IDs
	public static final int DRIVE_RIGHT_FRONT = 7,
							DRIVE_RIGHT_BACK = 9,
							DRIVE_LEFT_FRONT = 8,
							DRIVE_LEFT_BACK = 1;
	
	
	// Victor Ports					
	public static final int RAMP_MOTOR = 7,
							FRONT_ROLLER = 8,
							BACK_ROLLER = 9;
	
	//Servo ID's
	public static final int FLAP_FRONT_SERVO_ID = 3,
							FLAP_BACK_SERVO_ID = 2,
							LEFT_DRIVE_SERVO_ID = 0,
							RIGHT_DRIVE_SERVO_ID = 1;
	
	// Sensor ID's
	public static final int UP_SENSOR_ID = 4,
							DOWN_SENSOR_ID = 5;
	
	//Servo angle amounts
	public static final double GEAR_INTAKE_POSITION = 1.0,
							BALL_DUMPING_POSITION = 0.5,
							BALL_INTAKE_POSITION = 0.0,
							DRIVE_SERVO_SHIFT_HIGH_POSITION = 0.6,
							DRIVE_SERVO_SHIFT_LOW_POSITION = 0.4;
	
	// Joystick ports
	public static final int LEFT_JOYSTICK = 0,
							RIGHT_JOYSTICK = 1;
	
	// Buttons
	public static final int SHIFT_LOW_DRIVE = 8,
							SHIFT_HIGH_DRIVE = 9,
							LOW_GOAL = 3,
							RAMP_DOWN = 4,
							FLAP_GEAR_POSITION = 5,
							FLAP_BALL_INTAKE_POSITION = 6,
							ALIGN_TO_PEG = 7,
							SHIFT_LOW_CLIMB = 2; 
	
	// Vision constants
	public static final double TURNING_ANGLE_PROPORTION = 0.01,
							   DRIVING_DISTANCE_PROPORTION = (1.0/36.0),
							   TARGET_ANGLE_TOLERANCE = 0.5;
	
	// Quadrature encoders DIO ports
	public static final int QUAD_ENCODER_LEFT_1 = 2,
							QUAD_ENCODER_LEFT_2 = 3,
							QUAD_ENCODER_RIGHT_1 = 0,
							QUAD_ENCODER_RIGHT_2 = 1;
	
	
	// TO-DO: Change these values.
	public static final double WHEEL_DIAMETER_INCHES = 6.0,
							   DRIVE_GEAR_REDUCTION = 1.0,
							   ENCODER_TICKS_PER_REV = 250.0,
							   DEADBAND_DISTANCE = 4.0;
	
	public static final double MIN_DRIVE_POWER = 0.15;
	
	public static final double INTAKE_CLIMBING_SPEED = 0.6;
	
	public static double TICKS_PER_INCH = ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES*Math.PI);
	
	public static double ticksToInches(double ticks) {
		//System.out.println("Tick per inch: " + TICKS_PER_INCH);
		return ticks / TICKS_PER_INCH;
	}
	
	public static double inchesToTicks(double inches) {
		return inches * TICKS_PER_INCH; 
	}
	
	public static double radiansToDegrees(double radians) {
		return radians/Math.PI*180;
	}
	
	public static double degreesToRadians(double degrees) {
		return degrees/180*Math.PI;
	}
	
}
