package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class CameraData {
	double angle;
	long timestamp;
	
	public CameraData() {
		angle = 0;
		timestamp = 0;
	}
	
	public CameraData(double angle, long timestamp) {
		angle = this.angle;
		timestamp = this.timestamp;
	}

}
