package org.usfirst.frc.team1700.robot;

public class Pose {
	double angle;
	double distance;
	
	public Pose(double angle, double distance) {
		this.angle = angle;
		this.distance = distance;
	}
	
	public PoseDelta subtract(Pose otherPose) {
		PoseDelta poseDelta = new PoseDelta(this.angle - otherPose.angle, 
				this.distance - otherPose.distance);
		return poseDelta;
	}
	
	public Pose add(PoseDelta poseDelta) {
		Pose targetPose = new Pose(this.angle + poseDelta.angleDelta, 
				this.distance + poseDelta.distanceDelta);
		return targetPose;
	}
	
	public String toString() {
		return "Pose<" + this.angle + ", " + this.distance + ">";
	}

}