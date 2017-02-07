package org.usfirst.frc.team1700.robot;

import java.util.Collections;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private static final double HORIZONTAL_FOV = Constants.degreesToRadians(47);
	


	private double centerX;
	AxisCamera visionCamera;

	public final Object imgLock;
	private VisionThread visionThread;
	GripPipeline vpipeline;
	Mat image;
	NetworkTable table;
	
	
	// Dividing constant by number of pixels returns the distance in inches. 
	private static final double VISION_HEIGHT_CONSTANT = 3956;
	private static final double VISION_DISTANCE = 30;
	private static final double TARGET_HEIGHT_INCHES = 6;
	double angleDegrees;
	double pinholeAngleDegrees;

	//	private final NetworkTable table;

	public Vision() {
		centerX = 0.0;
		imgLock = new Object();
		vpipeline = new GripPipeline();
		table = NetworkTable.getTable("GRIP/myContoursReport");
		image = new Mat();

	}

	/**
	 * Called in robot init, starts vision processing. Adds vision camera and field camera. 
	 * Starts GRIP pipeline and finds two rectangles on target. 
	 * Calculates distance and angle to centerpoint of target.  
	 */
	public void initVision() {
		visionCamera = CameraServer.getInstance().addAxisCamera("axis-camera");
		visionCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		visionCamera.setExposureManual(8);
		
		CameraServer.getInstance().startAutomaticCapture(visionCamera);
		//		UsbCamera alignCamera = CameraServer.getInstance().startAutomaticCapture();

		
		// Starts loop for vision pipeline. 
		visionThread = new VisionThread(visionCamera, new GripPipeline(), pipeline -> {
			if (pipeline.filterContoursOutput().size() == 2) { //works if identifies two vision targets
				MatOfPoint mp1 = pipeline.filterContoursOutput().get(0);
				MatOfPoint mp2 = pipeline.filterContoursOutput().get(1);
				
				//still need to test if this works better/need to finish writing the code
				MatOfPoint2f pt1 = new MatOfPoint2f(mp1.toArray());
				MatOfPoint2f pt2 = new MatOfPoint2f(mp2.toArray());
				RotatedRect rect1 = Imgproc.minAreaRect(pt1);
				RotatedRect rect2 = Imgproc.minAreaRect(pt2);
				
				
				Rect r1 = Imgproc.boundingRect(mp1);
				Rect r2 = Imgproc.boundingRect(mp2);
				double d1 = getDistance(r1);
				double d2 = getDistance(r2);
				double distance = (d1+d2)/2;
				pinholeAngleDegrees = pinHole(r1, r2);
				table.putNumber("Time", System.currentTimeMillis());
				table.putNumber("Angle", pinholeAngleDegrees);
			}
		});
		visionThread.start();
	}
	
	public double pinHole(Rect rightRect, Rect leftRect) {
		double FOCAL_LENGTH =  IMG_WIDTH/(2*Math.tan(HORIZONTAL_FOV/2));
		System.out.println("Focal length" + FOCAL_LENGTH);
		double cx = IMG_WIDTH/2 - 0.5;
		double cy = IMG_HEIGHT/2 - 0.5;
		double rectCenter = ((rightRect.x - (leftRect.x + leftRect.width))/2 + leftRect.x + leftRect.width) + Constants.CAMERA_OFFSET;
		System.out.println("Rect center: " + rectCenter);
		double angleToTarget = Math.atan((rectCenter - cx)/FOCAL_LENGTH);
		return Constants.radiansToDegrees(angleToTarget);
	}

	/**
	 * Finds the robot's angle between centerline of camera image and centerline of target. 
	 * 
	 * @param rect1 First rectangle detected from camera input.
	 * @param rect2 Second rectangle detected from camera input. 
	 * @return angle Robot's angle to the target in radians. 
	 */
	public double getAngle(double distance, Rect rect1, Rect rect2) {
		double horizontalOffset = getHorizontalOffset(rect1, rect2);
		System.out.println(horizontalOffset);
		double angle = Math.asin(horizontalOffset/distance);
		return angle; 
	}

	/**
	 * 
	 * @param rect One of the rectangles on the vision target. 
	 * @return Distance from camera to target, returned in inches. 
	 */
	public double getDistance(Rect rect) {
		double height = rect.height;
		double distance = VISION_HEIGHT_CONSTANT/height;   
		return distance;
	}

	/**
	 * Finds the robot's horizontal offset in inches from the center of the target 
	 * using the input image from the camera. 
	 * 
	 * @param rect1 First rectangle detected from camera input. 
	 * @param rect2 Second rectangle detected from camera input. 
	 * @return horizontalOffset Robot's horizontal distance from the target in inches. 
	 */
	public double getHorizontalOffset(Rect rect1, Rect rect2) {
		Rect leftRect;
		Rect rightRect;
		if (rect1.x < rect2.x) {
			leftRect = rect1;
			rightRect = rect2;
		} else {
			leftRect = rect2;
			rightRect = rect1;
		}
		double rectMidpoint = (rightRect.x - (leftRect.x + leftRect.width))/2 + leftRect.x + leftRect.width;
		double picMidpoint = IMG_WIDTH / 2;
		double horizontalOffset = rectMidpoint - picMidpoint;
		System.out.println("Horizontal offset: " + horizontalOffset);
		double inchesPerPixel = TARGET_HEIGHT_INCHES/leftRect.height;
		horizontalOffset *= inchesPerPixel;


		return horizontalOffset; 
	}
}


