package org.usfirst.frc.team1700.robot;
/**
	@author christine
	The RobotMap should be usedfor naming all ports, ids, etc. 
	so we can change them more easily and ensure that we are not reusing them.
	*/
public class Constants {
	
	//Dont wan't people to instantiate
	private Constants() {} 
	
	//ID's for Motor Controllers
	public static final int DRIVE_RIGHT_FRONT = 1,
							DRIVE_RIGHT_BACK = 2,
							DRIVE_LEFT_FRONT = 3,
							DRIVE_LEFT_BACK = 4;
	
	//Joystick ports
	public static final int LEFT_JOYSTICK = 0,
							RIGHT_JOYSTICK = 1;
	
	//Buttons
	public static final int SHIFT_LOW = 1,
							SHIFT_HIGH = 2;
	
}
