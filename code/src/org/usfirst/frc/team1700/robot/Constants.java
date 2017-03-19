package org.usfirst.frc.team1700.robot;
/**
	The RobotMap should be used for naming all ports, ids, etc. 
	so we can change them more easily and ensure that we are not reusing them.
	*/
public class Constants {
	
	//Don't wan't people to instantiate
	private Constants() {

	} 
	public static class Values {
		public static class Drive {
			public static final double TURNING_ANGLE_PROPORTION = 0.085,
									   TURNING_ANGLE_D = -0.001,
									   TURNING_ANGLE_PROPORTION_LOW_GEAR = 0.02,
									   ANGLE_TOLERANCE = 5.0,
									   ANGLE_TOLERANCE_STOP = 1.0,
									   MAX_ANGLE_SPEED = 0.5,
									   DRIVING_DISTANCE_PROPORTION = (1.0/30.0),
									   DRIVING_DISTANCE_PROPORTION_LOW_GEAR = 1.0/200.0,
									   DRIVING_DISTANCE_D = -0.001,
									   DISTANCE_TOLERANCE = 4.0,
									   DISTANCE_TOLERANCE_STOP = 0.5,
									   MAX_DISTANCE_SPEED = 0.35,
									   MIN_DRIVE_POWER = 0.13,
									   MIN_DRIVE_POWER_LOW_GEAR = 0.3,
									   WHEEL_DIAMETER_INCHES = 4.0,
									   DRIVE_GEAR_REDUCTION = 1.0,
									   ENCODER_TICKS_PER_REV = 212.0,
									   INTAKE_CLIMBING_SPEED = 0.2,
									   TICKS_PER_INCH = ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES*Math.PI), 
									   VOLTAGE_RAMP_RATE = 100,
									   JOYSTICK_TUNING_CONSTANT = 0.4,
									   MID_RAMP_SPEED = 0.6;
									   
			public static final int MILLISECOND_HISTORY_LENGTH = 2000;
		}
		
		public static class Servos {
			public static final double GEAR_INTAKE_POSITION = 0.015,
					BALL_INTAKE_POSITION = 0.3,
					BALL_DUMPING_POSITION = 0.48,
					DRIVE_SERVO_SHIFT_HIGH_POSITION = 0.3,
					DRIVE_SERVO_SHIFT_LOW_POSITION = 0.7,
					FLAP_SERVO_OFFSET = 0.92;
		}
		public static class Auto {
			public static final double FIRST_DISTANCE = 104-18, 
									   TURN_ANGLE = 60,
									   SECOND_DISTANCE = 29,
									   BACK_UP_DISTANCE = 25,
									   TURN_MIDDLE_ANGLE = 90,
									   TO_SIDE_DISTANCE = 100,
									   CROSS_DISTANCE = 240,
									   MIDDLE_PEG_DISTANCE = 102-18, //TODO: Test
									   DEPLOY_TIME = 750, //in milliseconds
									   WAIT_TIME = 500;
		}

		public static class Vision {
			public static final int CAMERA_EXPOSURE_LOW = 1,
									CAMERA_EXPOSURE_NORMAL = 50,
									IMG_WIDTH = 640,
									IMG_HEIGHT = 360;
			public static final double CAMERA_RIGHT_OFFSET = 8.75,
									   CAMERA_LEFT_OFFSET = 8.75,
									   MAX_TARGET_HEIGHT_OFFSET = 30.0,
									   FILTER_DEADBAND = 10.0;
		}
		
		public static class LowGoal {
			public static final double LOW_MOTOR_VALUE = 100,
									   HIGH_MOTOR_VALUE = 150,
									   MOVE_UP_SPEED = 0.3,
									   MOVE_DOWN_SPEED = 0.4;
		}
		
		public static class Intake {
			public static final int CURRENT_LIMIT = 23;
		}
		
		public static class Climb {
			public static final int MAX_CURRENT = 50;
		}
		
		public static class Gear {
			public static final int POPPER_TIME = 500,
									DROPPER_TIME = 500;
		}
		
		public static class Field {
			public static final double PEG_VISION_SEPARATION = 8.25; 
		}
		
	}
	// CANTalon IDs
	public enum CanBus {
		DRIVE_LEFT_FRONT(1),
		DRIVE_LEFT_BACK(2),
		DRIVE_RIGHT_FRONT(3),
		DRIVE_RIGHT_BACK(4);
		
		private int id;
		
		private CanBus(int id) {
			this.id = id;
		}
		public int getId(){
			return this.id;
		}
	}
	

	public enum PWM {
		FLAP_LEFT(0),
		FLAP_RIGHT(1),
		// Victors
		FIRST_DUMPER_MOTOR(9),
		SECOND_DUMPER_MOTOR(8),
		FRONT_ROLLER(7),
		BACK_ROLLER(6);
		
		private int port;
		
		private PWM(int port) {
			this.port = port;
		}
		public int getPort(){
			return this.port;
		}
	}	
	
	// Joystick ports
	public enum DriverStation {
		LEFT_JOYSTICK(0),
		RIGHT_JOYSTICK(1),
		CO_JOYSTICK(2);
		
		private int port;
		
		private DriverStation(int port) {
			this.port = port;
		}
		public int getPort(){
			return this.port;
		}
	} 
	
	// Buttons
	public static class JoystickButtons{
		public enum Left {
			VISION(1),
			DEPLOY_GEAR(3),
			CLIMB(5);
			private int id;
			
			private Left(int id) {
				this.id = id;
			}
			public int getId(){
				return this.id;
			}
		} 
		
		public enum Right {
			SHIFT_LOW(3),
			SHIFT_HIGH(2);
			private int id;
			
			private Right(int id) {
				this.id = id;
			}
			public int getId(){
				return this.id;
			}
		}
		
		public enum Co {
//			LOW_GOAL_SCORE(1),
			DROPPER_RETRACT(1),
			GEAR_INTAKE(2),
			BALL_INTAKE(3),
			LOW_GOAL_UP(4),
			LOW_GOAL_DOWN(5),
			ANGLE(6),
			RESET(7),
			STOP_INTAKE(8);
			private int id;
			
			private Co(int id) {
				this.id = id;
			}
			public int getId(){
				return this.id;
			}
		} 
		
	} 
	
	public enum DigitalIO {
		QUAD_ENCODER_RIGHT_1(6),
		QUAD_ENCODER_RIGHT_2(7),
		QUAD_ENCODER_LEFT_1(8),
		QUAD_ENCODER_LEFT_2(9),
		HIGH_TOP_RAMP_ID(11),
		LOW_TOP_RAMP_ID(5),
//		GEAR_RECEIVER_SENSOR_ID(11),
		HIGH_BOTTOM_RAMP_ID(1),
		LOW_BOTTOM_RAMP_ID(2),
		FIRST_AUTO_SWITCH(3),
		SECOND_AUTO_SWITCH(4);
		private int port;
		
		private DigitalIO(int port) {
			this.port = port;
		}
		public int getPort(){
			return this.port;
		}
	}
	
	public enum AnalogInput {
		RAMP_RANGEFINDER(2);
		private int port;
		
		private AnalogInput(int port) {
			this.port = port;
		}
		
		public int getPort() {
			return this.port;
		}
	}
	
	public static final double RAMP_MOVEMENT_VERTICAL_DIST = 15.0,
							   RAMP_SLOW_DOWN_POINT = 0.5;
	
	public enum Solenoids {
		SHIFTER_1(6),
        SHIFTER_2(7),
        POPPER_1(4),
        POPPER_2(5),
        DROPPER_1(3),
        DROPPER_2(2);

		private int port;
		
		private Solenoids(int port) {
			this.port = port;
		}
		public int getPort(){
			return this.port;
		}
	}
			                	
	public static double ticksToInchesRight(double ticks) {
		//System.out.println("Tick per inch: " + TICKS_PER_INCH);
		return ticks / Values.Drive.TICKS_PER_INCH  * 212/360;
	}
	
	public static double ticksToInchesLeft(double ticks) {
		return ticks / Values.Drive.TICKS_PER_INCH;
	}
	
	public static double inchesToTicks(double inches) {
		return inches * Values.Drive.TICKS_PER_INCH; 
	}
	
	public static double radiansToDegrees(double radians) {
		return radians/Math.PI*180;
	}
	
	public static double degreesToRadians(double degrees) {
		return degrees/180*Math.PI;
	}
	
}
