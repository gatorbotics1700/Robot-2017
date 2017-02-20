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
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision {
	private static final double HORIZONTAL_FOV = Constants.degreesToRadians(70.42);
	private static final double VERTICAL_FOV = Constants.degreesToRadians(43.3);
	public static final double FOCAL_LENGTH = Constants.Values.Vision.IMG_WIDTH/(2*Math.tan(HORIZONTAL_FOV/2));


//	AxisCamera visionCamera;

	private VisionThread visionThread;
	GripPipeline vpipeline;
	Mat image;
	NetworkTable table;
	
	UsbCamera camera;
	
	
	private static final double VISION_DISTANCE = 30;
	private static final double TARGET_HEIGHT_INCHES = 5;
	private static final double MAX_DISTANCE = TARGET_HEIGHT_INCHES/2 / Math.tan(VERTICAL_FOV/2);
	// Dividing constant by number of pixels returns the distance in inches.
	private static final double VISION_HEIGHT_CONSTANT = Constants.Values.Vision.IMG_HEIGHT * MAX_DISTANCE;


	//	private final NetworkTable table;

	public Vision(int id) {
		vpipeline = new GripPipeline();
		table = NetworkTable.getTable("GRIP/myContoursReport");
		image = new Mat();

		camera = CameraServer.getInstance().startAutomaticCapture(id);
		
		camera.setResolution(Constants.Values.Vision.IMG_WIDTH, Constants.Values.Vision.IMG_HEIGHT);
		// Starts loop for vision pipeline. 
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			
			if (pipeline.filterContoursOutput().size() == 2) { //works if identifies two vision targets
				MatOfPoint mp1 = pipeline.filterContoursOutput().get(0);
				MatOfPoint mp2 = pipeline.filterContoursOutput().get(1);
				
				Rect r1 = Imgproc.boundingRect(mp1);
				Rect r2 = Imgproc.boundingRect(mp2);
				double d1 = getDistance(r1);
				double d2 = getDistance(r2);
				double distance = (d1+d2)/2;
				double pinholeAngle = pinHole(r1, r2);
				double h1 = getDistance(r1);
				CameraData cameraData = accountForCameraOffset(pinholeAngle, Constants.Values.Vision.CAMERA_RIGHT_OFFSET, distance);
				cameraData.timestamp = System.currentTimeMillis();
				table.putNumber("Time", cameraData.timestamp);
				table.putNumber("Angle", pinholeAngle);
				table.putNumber("Distance", distance);
				System.out.println("ID: " + id + " Angle: " + cameraData.angle + " Distance: "+ distance);
			}
			else {
				System.out.println("ID:" + id + "No targets detected");
			}
		});
		camera.setResolution(Constants.Values.Vision.IMG_WIDTH, Constants.Values.Vision.IMG_HEIGHT);

		visionThread.setPriority(1);
		visionThread.start();
		turnOffVision();
	}
	
	public void turnOnVision() {
		camera.setExposureManual(Constants.Values.Vision.CAMERA_EXPOSURE_LOW);
		//visionThread.notify();
	}
	
	public void turnOffVision() {
		camera.setExposureManual(Constants.Values.Vision.CAMERA_EXPOSURE_NORMAL);
//		try {
//			visionThread.wait();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * Called in robot init, starts vision processing. Adds vision camera and field camera. 
	 * Starts GRIP pipeline and finds two rectangles on target. 
	 * Calculates distance and angle to centerpoint of target.  
	 */
	
	public double pinHole(Rect rightRect, Rect leftRect) {
		double cx = Constants.Values.Vision.IMG_WIDTH/2 - 0.5;
		double cy = Constants.Values.Vision.IMG_HEIGHT/2 - 0.5;
		double rectCenter = ((rightRect.x - (leftRect.x + leftRect.width))/2 + leftRect.x + leftRect.width) + Constants.Values.Vision.CAMERA_RIGHT_OFFSET;
		return Math.atan((rectCenter - cx)/FOCAL_LENGTH);
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
	public static double getDistance(Rect rect) {
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
		double picMidpoint = Constants.Values.Vision.IMG_WIDTH / 2;
		double horizontalOffset = rectMidpoint - picMidpoint;
		System.out.println("Horizontal offset: " + horizontalOffset);
		double inchesPerPixel = TARGET_HEIGHT_INCHES/leftRect.height;
		horizontalOffset *= inchesPerPixel;


		return horizontalOffset; 
	}
	
	public static CameraData stereoImaging(CameraData firstCameraData, CameraData secondCameraData, double leftOffset, double rightOffset) {
		CameraData leftCamera;
		CameraData rightCamera;
		double leftAngle;
		double rightAngle;
		if(firstCameraData.angle > secondCameraData.angle) {
			leftCamera = firstCameraData;
			rightCamera = secondCameraData;
		} else {
			leftCamera = secondCameraData;
			rightCamera = firstCameraData;
		}
		
		//compute left and right angles to target
		if(leftCamera.angle * rightCamera.angle < 0) {
			leftAngle = Math.PI/2 - leftCamera.angle;
			rightAngle = Math.PI/2 - rightCamera.angle;
		} else if (leftCamera.angle > 0 && rightCamera.angle > 0) {
			leftAngle = Math.PI/2 - leftCamera.angle;
			rightAngle = Math.PI/2 + rightCamera.angle;
		} else {
			leftAngle =  Math.PI/2 + leftCamera.angle;
			rightAngle =  Math.PI/2 - rightCamera.angle;
		}
		double oppositeAngle = Math.PI - leftAngle - rightAngle;
		double leftDistance = Math.sin(oppositeAngle) / (leftOffset+rightOffset) * Math.sin(rightAngle);
		double rightDistance = Math.sin(oppositeAngle) / (leftOffset+rightOffset) * Math.sin(leftAngle);
		double centerDistance = Math.sqrt(Math.pow(leftDistance, 2) + Math.pow(leftOffset, 2) - 2*leftDistance*leftOffset*Math.cos(leftAngle));
		double centerAngle = Math.sin(centerDistance) / leftAngle * leftDistance;
		centerAngle -= Math.PI/2;
		return new CameraData(Math.max(firstCameraData.timestamp, secondCameraData.timestamp), centerAngle, centerDistance);
	}
	
	public static CameraData accountForCameraOffset(double pinholeAngle, double horizontalOffset, double hypWithoutOffset) {
		double horizontalDistNoOffset = Math.sin(pinholeAngle) * hypWithoutOffset;
		double verticalDistance = Math.sqrt(Math.pow(hypWithoutOffset, 2) - Math.pow(horizontalDistNoOffset, 2));
		double horizontalDistOffset = horizontalDistNoOffset + horizontalOffset;
		double distance = Math.sqrt(Math.pow(verticalDistance, 2) + Math.pow(horizontalDistOffset, 2));
		double angle = Math.asin(horizontalDistOffset/distance);
		return new CameraData(0, angle, distance);
	}
}


