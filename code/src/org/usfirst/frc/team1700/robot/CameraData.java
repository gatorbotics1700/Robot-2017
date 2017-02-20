package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class CameraData {
	long timestamp;
	double angle;
	double distance;
	
	public CameraData() {
		timestamp = 0;
		angle = 0;
		distance = 0;
	}
	
	public CameraData(long timestamp, double angle, double distance) {
		this.timestamp = timestamp;
		this.angle = angle;
		this.distance = distance;
	}
	
	public String toString() {
		return "CameraData@" + this.timestamp + "<" + this.angle + ", " + this.distance + ">"; 
	}

}
