package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Static class that contains game information.
 * The game information should survive the autonomous to driver controlled transition.
 * <p>
 *     That is, if we initialize the Alliance and starting position during autonomous,
 *     that information should be present during teleop.
 * </p>
 * <p>
 *     information: alliance, startPosition, lane?
 * </p>
 */
public class CenterStage {
    /** We can be on the BLUE or the RED alliance */
    enum Alliance {BLUE, RED}
    /** Our current alliance */
    public static Alliance alliance = Alliance.RED;

    /** We can start in the left or the right position */
    enum StartPos {AUDIENCE, BACKSTAGE}
    /** Starting Position of the Robot */
    public static StartPos startPos = StartPos.BACKSTAGE;

    public static void init() {
        setPose();
    }

    /**
     * Called during the Autonomous init_loop.
     * Obtains configuration information from the gamepad
     * @param gamepad1
     */
    public static void init_loop(Telemetry telemetry, Gamepad gamepad1) {
        // set the alliance
        if (gamepad1.x) {
            alliance = Alliance.BLUE;
            setPose();
        }
        if (gamepad1.b) {
            alliance = Alliance.RED;
            setPose();
        }

        // set the starting position
        if (gamepad1.dpad_up) {
            startPos = StartPos.BACKSTAGE;
            setPose();
        }
        if (gamepad1.dpad_down) {
            startPos = StartPos.AUDIENCE;
            setPose();
        }

        // possibly change starting conditions
        telemetry.addData("alliance: x (Blue) or b (Red)", alliance);
        telemetry.addData("startPos: dpad up or down", startPos);
    }


    /**
     * Set the robot pose based on alliance and starting position
     */
    public static void setPose() {
        double robotBackDistance = 7.75;
        double dx = 36.0;
        double fy = 70.75 - robotBackDistance;

        // Notes:
        // Blue alliance is A2 or A4 (tile y axis uses 1-based numbers)
        // Red alliance is F2or F4

        if (startPos == StartPos.AUDIENCE) {
            if (alliance == Alliance.RED) {
                // right side of the field
                Motion.setPoseInches(+dx, -fy, 180.0);
            } else {
                Motion.setPoseInches(-dx, -fy, 0.0);
            }
        } else {
            // starting position is RIGHT
            if (alliance == Alliance.RED) {
                Motion.setPoseInches(+dx, +fy, 180.0);
            } else {
                Motion.setPoseInches(-dx, +fy, 0.0);
            }
        }
    }

}
