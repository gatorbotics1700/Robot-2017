package org.usfirst.frc.team1700.robot;

public class PoseDelta {
	double angleDelta;
	double distanceDelta;
	
	public PoseDelta(double angleDelta, double distanceDelta) {
		this.angleDelta = angleDelta;
		this.distanceDelta = distanceDelta;
	}
	
	public boolean nearZero() {
		return (Math.abs(this.angleDelta) < Constants.Values.Drive.ANGLE_TOLERANCE && 
				Math.abs(this.distanceDelta) < Constants.DEADBAND_DISTANCE);
	}
	
	public String toString() {
		return "PoseDelta<" + this.angleDelta + ", " + this.distanceDelta + ">"; 
	}

}
