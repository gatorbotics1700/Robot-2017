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
	public static class Values {
		public static class Drive {
			public static final double TURNING_ANGLE_PROPORTION = 0.04,
									   DRIVING_DISTANCE_PROPORTION = (1.0/72.0),
									   ANGLE_TOLERANCE = 5.0,
									   DISTANCE_TOLERANCE = 4.0,
									   WHEEL_DIAMETER_INCHES = 4.0,
									   DRIVE_GEAR_REDUCTION = 1.0,
									   ENCODER_TICKS_PER_REV = 250.0,
									   MIN_DRIVE_POWER = 0.7,
									   INTAKE_CLIMBING_SPEED = 0.6,
									   TICKS_PER_INCH = ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES*Math.PI), 
									   VOLTAGE_RAMP_RATE = 12,
									   MID_RAMP_SPEED = 0.6;
			public static final int MILLISECOND_HISTORY_LENGTH = 2000;
		}
		
		public static class Servos {
			public static final double GEAR_INTAKE_POSITION = 1.0,
					BALL_DUMPING_POSITION = 0.5,
					BALL_INTAKE_POSITION = 0.0,
					DRIVE_SERVO_SHIFT_HIGH_POSITION = 0.3,
					DRIVE_SERVO_SHIFT_LOW_POSITION = 0.7;
		}
		public static class Auto {
			public static final double FIRST_DISTANCE = 50, 
									   TURN_ANGLE = 60,
									   SECOND_DISTANCE = 60,
									   BACK_UP_DISTANCE = 25,
									   MIDDLE_PEG_DISTANCE = 75, //TODO: Test
									   WAIT_TIME = 2;
		}

		public static class Vision {
			public static final int CAMERA_EXPOSURE = 1;
			public static final double CAMERA_OFFSET = 11.5,
									   MAX_TARGET_HEIGHT_OFFSET = 30.0;
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
		FLAP_BACK_SERVO_ID(0),
		FLAP_FRONT_SERVO_ID(1),
		// Victors
		FIRST_DUMPER_MOTOR(6),
		SECOND_DUMPER_MOTOR(7),
		FRONT_ROLLER(8),
		BACK_ROLLER(9);
		
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
		RIGHT_JOYSTICK(1);
		
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
			
			GEAR_INTAKE(1),
			LOW_GOAL_SCORE(2),
			BALL_INTAKE(3),
			CLIMB(4),
			DEFENSE(5),
			VISION(6);
			
			private int id;
			
			private Left(int id) {
				this.id = id;
			}
			public int getId(){
				return this.id;
			}
		} 
		
		public enum Right {
			ALIGN_TO_PEG(7);
			
			private int id;
			
			private Right(int id) {
				this.id = id;
			}
			public int getId(){
				return this.id;
			}
		} 
		
	} 
	
	public enum DigitalIO {
		QUAD_ENCODER_RIGHT_1(0),
		QUAD_ENCODER_RIGHT_2(1),
		QUAD_ENCODER_LEFT_1(2),
		QUAD_ENCODER_LEFT_2(3),
		TOP_SENSOR_ID(4),
		BOTTOM_SENSOR_ID(5),
		GEAR_RECEIVER_SENSOR_ID(6),
		RAMP_HEIGHT_SENSOR_ID(7),
		FIRST_AUTO_SWITCH(8),
		SECOND_AUTO_SWITCH(9);
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
        RETRACTOR_1(4),
        RETRACTOR_2(5);

		private int port;
		
		private Solenoids(int port) {
			this.port = port;
		}
		public int getPort(){
			return this.port;
		}
	}
			                	
	public static double ticksToInches(double ticks) {
		//System.out.println("Tick per inch: " + TICKS_PER_INCH);
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
