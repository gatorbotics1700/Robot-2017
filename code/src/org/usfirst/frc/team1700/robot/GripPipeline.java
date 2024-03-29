package org.usfirst.frc.team1700.robot;

import java.util.ArrayList;
import java.util.List;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import org.opencv.core.*;
import org.opencv.imgproc.*;
/* import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.*; */

/**
* GripPipeline class.
*
* <p>An OpenCV pipeline generated by GRIP.
* Filters so camera only identifies two contours-the two vision targets on either side of the peg
*
* @author GRIP
*/
public class GripPipeline implements VisionPipeline {

	//Outputs
	private Mat blurOutput = new Mat();
	private Mat hslThresholdOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	@Override	public void process(Mat source0) {
		// Step Blur0:
		Mat blurInput = source0;
		BlurType blurType = BlurType.get("Box Blur");
		double blurRadius = 0.0;
		blur(blurInput, blurType, blurRadius, blurOutput);

		// Step HSL_Threshold0:
		Mat hslThresholdInput = blurOutput;
		double[] hslThresholdHue = {62, 178}; //test 50-70
		double[] hslThresholdSaturation = {57, 255.0};
		double[] hslThresholdLuminance = {214, 255.0};
		hslThreshold(hslThresholdInput, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance, hslThresholdOutput);

		// Step Find_Contours0:
		Mat findContoursInput = hslThresholdOutput;
		boolean findContoursExternalOnly = false;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

		// Step Filter_Contours0:
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		double filterContoursMinArea = 10.0;
		double filterContoursMinPerimeter = 0.0;
		double filterContoursMinWidth = 0.0;
		double filterContoursMaxWidth = 1000.0;
		double filterContoursMinHeight = 0.0;
		double filterContoursMaxHeight = 1000.0;
		double[] filterContoursSolidity = {0, 100};
		double filterContoursMaxVertices =100.0;
		double filterContoursMinVertices = 0.0;
		double filterContoursMinRatio = 0.0;
		double filterContoursMaxRatio = 1000.0;
		double almostVerticalSlope = 1.5;
		double almostHorizontalSlope = 0.8;
		filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, almostVerticalSlope, almostHorizontalSlope, filterContoursOutput);

	}

	/** 
	 * This method is a generated getter for the output of a Blur.
	 * @return Mat output from Blur.
	 */
	public Mat blurOutput() {
		return blurOutput;
	}

	/**
	 * This method is a generated getter for the output of a HSL_Threshold.
	 * @return Mat output from HSL_Threshold.
	 */
	public Mat hslThresholdOutput() {
		return hslThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Contours.
	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
	 */
	public ArrayList<MatOfPoint> filterContoursOutput() {
		return filterContoursOutput;
	}


	/**
	 * An indication of which type of filter to use for a blur.
	 * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
	 */
	enum BlurType{
		BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
			BILATERAL("Bilateral Filter");

		private final String label;

		BlurType(String label) {
			this.label = label;
		}

		public static BlurType get(String type) {
			if (BILATERAL.label.equals(type)) {
				return BILATERAL;
			}
			else if (GAUSSIAN.label.equals(type)) {
			return GAUSSIAN;
			}
			else if (MEDIAN.label.equals(type)) {
				return MEDIAN;
			}
			else {
				return BOX;
			}
		}

		@Override
		public String toString() {
			return this.label;
		}
	}

	/**
	 * Softens an image using one of several filters.
	 * @param input The image on which to perform the blur.
	 * @param type The blurType to perform.
	 * @param doubleRadius The radius for the blur.
	 * @param output The image in which to store the output.
	 */
	private void blur(Mat input, BlurType type, double doubleRadius,
		Mat output) {
		int radius = (int)(doubleRadius + 0.5);
		int kernelSize;
		switch(type){
			case BOX:
				kernelSize = 2 * radius + 1;
				Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
				break;
			case GAUSSIAN:
				kernelSize = 6 * radius + 1;
				Imgproc.GaussianBlur(input,output, new Size(kernelSize, kernelSize), radius);
				break;
			case MEDIAN:
				kernelSize = 2 * radius + 1;
				Imgproc.medianBlur(input, output, kernelSize);
				break;
			case BILATERAL:
				Imgproc.bilateralFilter(input, output, -1, radius, radius);
				break;
		}
	}

	/**
	 * Segment an image based on hue, saturation, and luminance ranges.
	 *
	 * @param input The image on which to perform the HSL threshold.
	 * @param hue The min and max hue
	 * @param sat The min and max saturation
	 * @param lum The min and max luminance
	 * @param output The image in which to store the output.
	 */
	private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum,
		Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
		Core.inRange(out, new Scalar(hue[0], lum[0], sat[0]),
			new Scalar(hue[1], lum[1], sat[1]), out);
	}

	/**
	 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
	 * @param input The image on which to perform the Distance Transform.
	 * @param type The Transform.
	 * @param maskSize the size of the mask.
	 * @param output The image in which to store the output.
	 */
	private void findContours(Mat input, boolean externalOnly,
		List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}


	/**
	 * Filters out contours that do not meet certain criteria.
	 * @param inputContours is the input list of contours
	 * @param output is the the output list of contours
	 * @param minArea is the minimum area of a contour that will be kept
	 * @param minPerimeter is the minimum perimeter of a contour that will be kept
	 * @param minWidth minimum width of a contour
	 * @param maxWidth maximum width
	 * @param minHeight minimum height
	 * @param maxHeight maximimum height
	 * @param Solidity the minimum and maximum solidity of a contour
	 * @param minVertexCount minimum vertex Count of the contours
	 * @param maxVertexCount maximum vertex Count
	 * @param minRatio minimum ratio of width to height
	 * @param maxRatio maximum ratio of width to height
	 */
	
	private void filterContours(List<MatOfPoint> inputContours, double minArea,
		double minPerimeter, double minWidth, double maxWidth, double minHeight, double
		maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
		minRatio, double maxRatio, double almostVerticalSlope, double almostHorizontalSlope, List<MatOfPoint> output) {
		final MatOfInt hull = new MatOfInt();
		output.clear();
		ArrayList<MatOfPoint> filtered = new ArrayList<MatOfPoint>();
		double minTargetDistanceFound = 652.0; //initialized to field length
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			
			//filter by size
			//TODO: Look through filters and see what we actually need
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth) continue;
			if (bb.height < minHeight || bb.height > maxHeight) continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea) continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int)hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1]) continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
			final double ratio = bb.width / (double)bb.height;
			if (ratio < minRatio || ratio > maxRatio) continue;
			
			
			// filter by shape
//			final double SLOPE_DIFFERENCE = 1.0; 
//			double[] slopes = new double[4]; 
//			Point[] points = contour.toArray();
//			double slope = 0.0;
//			for(int k=0; k<4; k++) {
//				double dx = points[k].x - points[(k+1) % 4].x;
//				double dy = points[k].y - points[(k+1) % 4].y;
//				if (dx == 0) {
//					dx = .01;
//				}
//				slope = dy/dx;
//				slopes[k] = slope; 
//				System.out.println("Slope:" + slope);
//			}	
//			Arrays.sort(slopes);
//			if (Math.abs(slopes[1] - slopes[0]) > SLOPE_DIFFERENCE ||
//					Math.abs(slopes[3] - slopes[2]) > SLOPE_DIFFERENCE) {
//				continue;
//			}
			filtered.add(contour);
		}
		for (int i = 0; i < filtered.size(); i++) {
			for (int j = i + 1; j < filtered.size(); j++) {
				double avgDist = avgDistance(filtered.get(i), filtered.get(j));
				if (isValidPair(filtered.get(i),filtered.get(j))) {
					if (avgDist < minTargetDistanceFound) {
						output.clear();
						minTargetDistanceFound = avgDist;
						output.add(filtered.get(i));
						output.add(filtered.get(j));
					}
					continue;
				}
			}
		}
	}

	private boolean isValidPair(MatOfPoint contour1, MatOfPoint contour2) {
		Rect r1 = Imgproc.boundingRect(contour1);
		Rect r2 = Imgproc.boundingRect(contour2);
		double dist1 = Vision.getDistance(r1);
		double dist2 = Vision.getDistance(r2);
		double maxHorizontalSeparation = Constants.Values.Field.PEG_VISION_SEPARATION*Vision.FOCAL_LENGTH/((dist1+dist2)/2) 
				+ Constants.Values.Vision.FILTER_DEADBAND;
		double maxVerticalSeparation = (r1.height + r2.height)/2 + Constants.Values.Vision.FILTER_DEADBAND;
//		if (Math.abs(dist1-dist2) > Constants.Values.Field.PEG_VISION_SEPARATION) {
//		System.out.println("Depth offset: " + Math.abs(dist1-dist2));
//		System.out.println("Max depth offset: " + Constants.Values.Field.PEG_VISION_SEPARATION);
////			return false;
//			
//		} 
//		if (Math.abs(r1.x - r2.x) > maxHorizontalSeparation) {
//			System.out.println("Horizontal Distance: " + Math.abs(r1.x - r2.x));
//			System.out.println("Max Distance: " + maxHorizontalSeparation);
//			return false;
//		}
//		if (Math.abs((r1.y+r1.height/2)-(r2.y+r2.height/2)) > maxVerticalSeparation) {
//			System.out.println("Height offset: " + Math.abs((r1.y+r1.height/2)-(r2.y+r2.height/2)));
//			System.out.println("Max height offset: " + maxVerticalSeparation);
//			return false;
//		}
		return true;
	}
	
	private double avgDistance(MatOfPoint contour1, MatOfPoint contour2) {
		Rect r1 = Imgproc.boundingRect(contour1);
		Rect r2 = Imgproc.boundingRect(contour2);
		double dist1 = Vision.getDistance(r1);
		double dist2 = Vision.getDistance(r2);
		return (dist1+dist2)/2;
	}

}

