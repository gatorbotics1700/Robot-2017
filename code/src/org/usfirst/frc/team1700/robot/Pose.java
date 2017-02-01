package org.usfirst.frc.team1700.robot;

public class Pose {
	double angle;
	double distance;
	
	public Pose(double angle, double distance) {
		this.angle = angle;
		this.distance = distance;
	}
	
	public PoseDelta subtract(Pose targetPose) {
		PoseDelta poseDelta = new PoseDelta(targetPose.angle - this.angle, 
				targetPose.distance - this.distance);
		return poseDelta;
	}
	
	public Pose add(PoseDelta poseDelta) {
		Pose targetPose = new Pose(this.angle + poseDelta.angleDelta, 
				this.distance + poseDelta.distanceDelta);
		return targetPose;
	}
	

}