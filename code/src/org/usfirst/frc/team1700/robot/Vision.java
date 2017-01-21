package org.usfirst.frc.team1700.robot;

import java.util.Collections;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.imgproc.*;

public class Vision {
	Drive drive;
	private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	
	private double centerX;
	AxisCamera visionCamera;
	
	private final Object imgLock;
	private VisionThread visionThread;
	GripPipeline vpipeline;
	Mat image;
	private static final double VISION_HEIGHT_CONSTANT = 3956;
	private static final double VISION_DISTANCE = 30;
	private static final double TARGET_HEIGHT_INCHES = 6;

//	private final NetworkTable table;
	
	public Vision() {
		drive = new Drive();
		centerX = 0.0;
		imgLock = new Object();
		vpipeline = new GripPipeline();
//		table = NetworkTable.getTable("GRIP/myContoursReport");
		image = new Mat();
		
	}
	
	public void initVision(Joystick joystick) {
		visionCamera = CameraServer.getInstance().addAxisCamera("axis-camera");
		visionCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		CameraServer.getInstance().startAutomaticCapture(visionCamera);
//		UsbCamera alignCamera = CameraServer.getInstance().startAutomaticCapture();

		visionThread = new VisionThread(visionCamera, new GripPipeline(), pipeline -> {
				if (pipeline.filterContoursOutput().size() == 2) { //works if identifies two vision targets
					
//					for(MatOfPoint value: pipeline.filterContoursOutput()) {
					MatOfPoint mp1 = pipeline.filterContoursOutput().get(0);
					MatOfPoint mp2 = pipeline.filterContoursOutput().get(1);
					Rect r1 = Imgproc.boundingRect(mp1);
					Rect r2 = Imgproc.boundingRect(mp2);
					double d1 = getDistance(r1);
					double d2 = getDistance(r2);
					double distance = (d1+d2)/2;
					double angle = getAngle(distance, r1, r2);
					System.out.println("Angle: " + angle);
					System.out.println("Distance: " + distance);
            synchronized (imgLock) {
				double angleRadians = angle/Math.PI*180;
				if (joystick.getRawButton(8)) {
					drive.turnToAngle(angleRadians);
				}
//            	System.out.println("Rect bound:" + r.area());
//                centerX = r.x + (r.width / 2);
//            }
            }
        }
    });
    visionThread.start();
	}
	
	/**
	 * Finds the robot's angle to the target. 
	 * 
	 * @param rect1 First rectangle detected from camera input.
	 * @param rect2 Second rectangle detected from camera input. 
	 * @return angle Robot's angle to the target. 
	 */
	public double getAngle(double distance, Rect rect1, Rect rect2) {
		double horizontalOffset = getHorizontalOffset(rect1, rect2);
		System.out.println(horizontalOffset);
		double angle = Math.asin(horizontalOffset/distance);
		return angle; 
	}
	
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
		double inchesPerPixel = TARGET_HEIGHT_INCHES/leftRect.height;
		horizontalOffset *= inchesPerPixel;
		System.out.println("Rect 1 x: " + rect1.x);
		System.out.println("Rect 2 x: " + rect2.x);
		System.out.println("Rect 1 width: " + rect1.width);
		System.out.println("Rect 2 width: " + rect2.width);

		return horizontalOffset; 
	}
		
	public void findContours() {

//			CameraServer.getInstance().getVideo(visionCamera).grabFrame(image);
//			vpipeline.process(image);
//			vpipeline.filterContoursOutput();
//			
//			for(MatOfPoint value: vpipeline.filterContoursOutput()) {
//				System.out.println(value.toList());
//				double contourArea = Imgproc.contourArea(value);
//				System.out.println("Contour area" + contourArea);
//				Rect boundRect = Imgproc.boundingRect(value);
//				System.out.println("Rect area: " + boundRect.area());
//				Timer.delay(1);
//			}
		
//			

		
//		for (double area : table.getNumberArray("targets/area", new double[0])) {
//            System.out.println("Got contour with area=" + area);
//        }
//		
//		while(true) {
//			double[] areas = table.getNumberArray("area", defaultValue);
//			System.out.print("areas: ");
//			for(double area: areas) {
//				System.out.print(area + " ");
//			}
//			System.out.println();
//			Timer.delay(1);
//		}
//	}
	}
	
//	
//	
//	
//
}


