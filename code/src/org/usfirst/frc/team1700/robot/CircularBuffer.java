package org.usfirst.frc.team1700.robot;

import java.util.ArrayList;

public class CircularBuffer {
	
	ArrayList<Pose> circularBuffer;
	int currentPos;
	
	public CircularBuffer(double historyLength) {
		circularBuffer = new ArrayList<Pose>(historyLength);
		currentPos = 0;
	}
	
	public void add(Pose pose) {
		currentPos++;
		currentPos %= circularBuffer.size();
		circularBuffer.add(currentPos, pose);
	}
	
	public Pose getHistory(double millisecondOffset) {
		int firstOffset = Int(millisecondOffset/50);
		int secondOffset = firstIndex + 1; 
		double firstError = millisecondOffset - (firstOffset * 50);
		double secondError = (secondOffset * 50) - millisecondOffset;
		double firstProp = secondError / 50.0; 
		double secondProp = firstError / 50.0; 
		int firstIndex = (currentPos - firstOffset)%circularBuffer.size();
		if (firstIndex < 0) {
			firstIndex += circularBuffer.size();
		}
		int secondIndex = (currentPos - secondOffset)%circularBuffer.size();
		if (secondIndex < 0) {
			secondIndex += circularBuffer.size();
		}
		Pose firstPose = circularBuffer.get(firstIndex);
		Pose secondPose = circularBuffer.get(secondIndex);
		double newAngle = firstPose.angle * firstProp + secondPose.angle * secondProp; 
		double newDistance = firstPose.distance * firstProp + secondPose.distance * secondProp; 
		Pose newPose = new Pose(newAngle, newDistance);
		return newPose;
	}
}
