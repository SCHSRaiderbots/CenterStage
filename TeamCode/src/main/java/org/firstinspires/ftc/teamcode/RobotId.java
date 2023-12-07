package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The possible robot identities.
 */
public enum RobotId {
    /** CenterStage */
    ROBOT_2023("robot2023"),
    /** PowerPlay */
    ROBOT_2022("robot2022"),
    /** Freight Frenzy Robot */
    ROBOT_2021("robot2021"),
    /** Ultimate Goal */
    ROBOT_2020("robot2020"),
    /** Skystone */
    ROBOT_2019("robot2019"),
    /** Rover Ruckus */
    ROBOT_2018("robot2018"),
    /** Mecanum */
    ROBOT_MECANUM("robotMecanum");

    /** Name of the fake RevTouchSensor for this robot */
    public final String stringName;

    /** Identity of the current robot -- defaults to ROBOT_2023 */
    public static RobotId robot = ROBOT_2023;

    /**
     * Constructor
     * @param name Name of the pseudo TouchSensor in the configuration
     */
    RobotId(String name) {
        stringName = name;
    }

    /**
     * Look at the hardwareMap to figure out which robot is being used.
     * @param hardwareMap the robot configuration information
     */
    static RobotId identifyRobot(HardwareMap hardwareMap) {
        // look at all possible RobotIds...
        for (RobotId rid : RobotId.values()) {
            // see whether there is a TouchSensor with the robot's name
            RevTouchSensor touch = hardwareMap.tryGet(RevTouchSensor.class, rid.stringName);

            if (touch != null) {
                // found a match
                robot = rid;
                break;
            }
        }

        // we did not find any evidence to contrary, assume robot was set correctly...
        return robot;
    }
}
