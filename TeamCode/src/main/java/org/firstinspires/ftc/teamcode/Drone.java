package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Drone {
    Servo servoDrone;
    /** This position has the servo arm slightly back from vertical to hold the loop */
    static final double posArmed = 0.4;
    /** This position has the servo arm horizontal to release the loop */
    static final double posFired = 0.85;

    public Drone(HardwareMap hardwareMap) {
        // get the servo (if it exists)
        servoDrone = hardwareMap.tryGet(Servo.class, "servoDrone");

        // initialize the arm to hold the loop
        setLaunch(false);
    }

    /**
     * Position the launch mechanism.
     * All servo positioning commands route through this method.
     * The method is a no-op if the drone servo does not exist.
     * @param booleanLaunch true is to launch the drone; false to hold the drone
     */
    public void setLaunch(boolean booleanLaunch) {
        if (servoDrone != null) {
            setAbsolutePosition((booleanLaunch) ? posFired : posArmed);
        }
    }

    /**
     * Move the drone servo
     * @param position Servo position in range 0 to 1.
     */
    public void setAbsolutePosition(double position) {
        // only move the servo if it is there.
        if (servoDrone != null) {
            // should be safe to command the hardware servo
            servoDrone.setPosition(position);
        }
    }
}
