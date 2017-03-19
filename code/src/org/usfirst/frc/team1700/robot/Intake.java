/* This class handles the movement of the front and back intake rollers. Motors include:
 * Victors for the front and back intake rollers
 * 
 */
package org.usfirst.frc.team1700.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Victor;

public class Intake {
	Victor frontRoller;
	Victor backRoller;
	PowerDistributionPanel pdp;
	
	public Intake(){
		frontRoller = new Victor(Constants.PWM.FRONT_ROLLER.getPort());
		backRoller = new Victor(Constants.PWM.BACK_ROLLER.getPort());
		pdp = new PowerDistributionPanel();
	}
	
	//This method runs the intake mechanism used during most of the match
	public void runIntake(){
		frontRoller.set(Constants.Values.Intake.CURRENT_LIMIT / Math.abs(pdp.getCurrent(4)));
		//backRoller.set(-Constants.Values.Intake.CURRENT_LIMIT / Math.abs(pdp.getCurrent(6)));
		backRoller.set(-1);
	}
	
	//This method stops the intake rollers.
	public void stopIntake(){
		frontRoller.set(0);
		backRoller.set(0);
	}
	
	//This method is used when the robot "shoots" into the low goal by running the front roller backwards.
	public void lowGoalIntake() {
		frontRoller.set(-0.4);
		backRoller.set(0);
	}
	
	//This method is used for climbing, when only the back roller is running for the purposes of gripping and scaling the Velcro rope.
	public void climbIntake() {
		frontRoller.set(0);
		backRoller.set(-Constants.Values.Drive.INTAKE_CLIMBING_SPEED);
	}
}

