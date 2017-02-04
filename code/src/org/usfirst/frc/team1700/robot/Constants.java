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
			public static final double TURNING_ANGLE_PROPORTION = 0.01,
									   DRIVING_DISTANCE_PROPORTION = (1.0/36.0),
									   ANGLE_TOLERANCE = 0.5,
									   DISTANCE_TOLERANCE = 4.0,
									   WHEEL_DIAMETER_INCHES = 6.0,
									   DRIVE_GEAR_REDUCTION = 1.0,
									   ENCODER_TICKS_PER_REV = 250.0,
									   MIN_DRIVE_POWER = 0.15,
									   INTAKE_CLIMBING_SPEED = 0.6,
									   TICKS_PER_INCH = ENCODER_TICKS_PER_REV * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES*Math.PI);
									   
		}
		public static class Servos {
			public static final double GEAR_INTAKE_POSITION = 1.0,
					BALL_DUMPING_POSITION = 0.5,
					BALL_INTAKE_POSITION = 0.0,
					DRIVE_SERVO_SHIFT_HIGH_POSITION = 0.6,
					DRIVE_SERVO_SHIFT_LOW_POSITION = 0.4;
		}
	}
	// CANTalon IDs
	public enum CanBus {
		//TODO: Change ID's
		DRIVE_LEFT_BACK(1),
		DRIVE_RIGHT_FRONT(7),
		DRIVE_LEFT_FRONT(8),
		DRIVE_RIGHT_BACK(9);
		
		private int id;
		
		private CanBus(int id) {
			this.id = id;
		}
		public int getId(){
			return this.id;
		}
	}

	public enum PWM {
		LEFT_DRIVE_SERVO_ID(0),
		RIGHT_DRIVE_SERVO_ID(1),
		FLAP_BACK_SERVO_ID(2),
		FLAP_FRONT_SERVO_ID(3),
		DUMPER_MOTOR(7),
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
			EXTEND_GEAR(1),
			SHIFT_LOW_CLIMB(2),
			DUMPER_UP(3),
			DUMPER_DOWN(4),
			FLAP_GEAR_POSITION(5),
			FLAP_BALL_INTAKE_POSITION(6),
			SHIFT_LOW_DRIVE(8),
			SHIFT_HIGH_DRIVE(9);
			
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
	
	// Quadrature encoders DIO ports
	public enum DigitalIO {
		QUAD_ENCODER_RIGHT_1(0),
		QUAD_ENCODER_RIGHT_2(1),
		QUAD_ENCODER_LEFT_1(2),
		QUAD_ENCODER_LEFT_2(3),
		UP_SENSOR_ID(4),
		DOWN_SENSOR_ID(5);

		private int port;
		
		private DigitalIO(int port) {
			this.port = port;
		}
		public int getPort(){
			return this.port;
		}
	}
	
	public enum Solenoids {
		SHIFTER_1(1),
        SHIFTER_2(2),
        RETRACTOR_1(3),
        RETRACTOR_2(4);

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
