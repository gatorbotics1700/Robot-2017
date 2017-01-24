package org.usfirst.frc.team1700.robot;
/**
	@author christine
	The RobotMap should be usedfor naming all ports, ids, etc. 
	so we can change them more easily and ensure that we are not reusing them.
	*/
public class Constants {
	
	//Don't wan't people to instantiate
	private Constants() {

	} 
	
	//ID's for Motor Controllers
	//TODO: Change CANTalon ID's for actual robot
	public static final int DRIVE_RIGHT_FRONT = 0,
							DRIVE_RIGHT_BACK = 10,
							DRIVE_LEFT_FRONT = 3,
							DRIVE_LEFT_BACK = 8,
							RAMP_MOTOR = 1;
	
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
	public static final double TURNING_ANGLE_PROPORTION = 0.1,
							   DRIVING_DISTANCE_PROPORTION = (1.0/48.0),
							   TARGET_ANGLE_TOLERANCE = 5;
	
	//Quadrature encoders DIO ports
	public static final int QUAD_ENCODER_LEFT_1 = 2,
							QUAD_ENCODER_LEFT_2 = 3,
							QUAD_ENCODER_RIGHT_1 = 0,
							QUAD_ENCODER_RIGHT_2 = 1;
	
	//TO-DO: Change these values.
	public static final double WHEEL_DIAMETER_INCHES = 6.0,
							   DRIVE_GEAR_REDUCTION = 1.0,
							   ENCODER_TICKS_PER_REV = 250.0;
	
	public static double TICKS_PER_INCH = ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES*Math.PI);
	
	public static double ticksToInches(double ticks) {
		System.out.println("Tick per inch: " + TICKS_PER_INCH);
		return ticks / TICKS_PER_INCH;
	}
	
	public static double inchesToTicks(double inches) {
		return inches * TICKS_PER_INCH; 
	}
	
}
