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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.imgproc.*;

public class Vision {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	
	private double centerX;
	AxisCamera visionCamera;
	
	private final Object imgLock;
	private VisionThread visionThread;
	GripPipeline vpipeline;
	Mat image;
	private static final int VISION_HEIGHT_CONSTANT = 30;
//	private final NetworkTable table;
	
	public Vision() {
		centerX = 0.0;
		imgLock = new Object();
		vpipeline = new GripPipeline();
//		table = NetworkTable.getTable("GRIP/myContoursReport");
		image = new Mat();
		
	}
	
	public void initVision() {
		visionCamera = CameraServer.getInstance().addAxisCamera("axis-camera");
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
					System.out.println("Rect 1 distance: " + d1);
					System.out.println("Rect 2 distance: " + d2);
					System.out.println("Rect 1 area: " + r1.area());
					System.out.println("Rect 2 area: " + r2.area());
//					}
			System.out.println("");
            synchronized (imgLock) {
//            	System.out.println("Rect bound:" + r.area());
//                centerX = r.x + (r.width / 2);
            }
        }
    });
    visionThread.start();
	}
	
	public double getDistance(Rect rect) {
		double height = rect.height;
		double distance = VISION_HEIGHT_CONSTANT/height;
		return distance;
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


