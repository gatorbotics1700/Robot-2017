/* This class stores information about a change in robot pose.
 * It stores two values:
 * angleDelta, which tracks the change in angle
 * distanceDelta, which tracks the change in distance
 * 
 */
package org.usfirst.frc.team1700.robot;

public class PoseDelta {
	double angleDelta;
	double distanceDelta;
	
	public PoseDelta(double angleDelta, double distanceDelta) {
		this.angleDelta = angleDelta;
		this.distanceDelta = distanceDelta;
	}
	
	//This method returns whether or not the poseDelta is near zero.
	public boolean nearZero() {
		return (Math.abs(this.angleDelta) < Constants.Values.Drive.ANGLE_TOLERANCE_STOP && 
				Math.abs(this.distanceDelta) < Constants.Values.Drive.DISTANCE_TOLERANCE_STOP);
	}
	
	public boolean withinTol() {
		return (Math.abs(this.angleDelta) < Constants.Values.Drive.ANGLE_TOLERANCE && 
				Math.abs(this.distanceDelta) < Constants.Values.Drive.DISTANCE_TOLERANCE);
	}
	
	public boolean speedTol() {
		return (Math.abs(this.angleDelta) < Constants.Values.Drive.ANGLE_TOLERANCE && 
				Math.abs(this.distanceDelta) < Constants.Values.Drive.DISTANCE_TOLERANCE);
	}
	
	//A method to turn the PoseDelta object into strings for easy debugging.
	public String toString() {
		return "PoseDelta<" + this.angleDelta + ", " + this.distanceDelta + ">"; 
	}

}
