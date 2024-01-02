package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
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

    private static void zx(int tag, float dx, float dy, float dz) {
        // rotate by 60 around the x to make the image almost vertical
        float rx = 60.0f;
        // no rotation about the y axis
        float ry = 0.0f;
        // rotate about z axis
        float rz = -90.0f;
        OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz))
                .multiplied(OpenGLMatrix.translation(0.0f, 4.875f, 0.0f));
    }

    private static void kk(int tag, float dx, float dy, float dz) {
        // rotate by 90 around the x to make the image vertical
        float rx = 90.0f;
        // no rotation about the y axis
        float ry = 0.0f;
        // rotate 90 so picture z is pointing into arena.
        float rz = 90.0f;
        // mumble
        OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz));
    }

    public static void init() {
        // figure out positions of the April Tags
        // Tags 1 through 6: 6 inches apart
        float inchTile = 22.75f;
        // the x value is constant
        float x = 2.0f * inchTile + 12.5f;
        float y = 1.5f * inchTile;
        float dy = 6.0f;
        float z = 0.0f;

        // 1 is at -x-6 (FTC says 60.3, 41.4, 4.0)
        zx(1, x, y+dy, z);
        // 2 is at -x  (FTC says 60.3, 35.4, 4.0)
        zx(2, x, y, z);
        // 3 is at -x+6 (FTC says 600.3, 29.4, 4.0)
        zx(3, x, y-dy, z);
        // 4 is at x-6
        zx(4, x, -y+dy, z);
        // 5 is at x
        zx(5, x, -y, z);
        // 6 is at x+6
        zx(6, x, -y-dy, z);

        // 7-10
        // The small (2 inch by 2 inch) 8 and 9 centers are 4 inches above the deck
        // The large (5 inch by 5 inch) are dx = 5.5 inches and dz = 1.5 inch
        // The dx distance can be calculated. Tags are 2 inches apart.
        //   So dx = 2.0 + 0.5 * 2 + 0.5 * 5 = 5.5 inches
        // The dz can be calculated because the bottoms of the April Tags are aligned.
        //   thus center of 5 inch is +2.5, center of 2 inch is +1.0; diff is 1.5 inch
        float ySmall = 1.0f * inchTile + 11.0f;
        float yLarge = ySmall + 5.5f;
        // the small April Tags are 4.0 inches above the deck
        float zSmall = 4.0f;
        // the large April Tags are 4.0 + 2.5 inches above the deck
        float zLarge = 4.0f + 2.5f;

        // x value is constant
        x = -3.0f * inchTile - 0.5f;

        // now position the tags...
        // 7 is a +dx and +dy (FTC says -70.3, -40.6, 5.5)
        kk(7, x, -yLarge, zLarge);
        // 8 is a 1 tile + 11 inches (FTC says -70.3, -35.1, 4.0)
        kk(8, x, -ySmall, zSmall);
        // 9 is at -1 tile - 11 inches
        kk(9, x, ySmall, zSmall);
        // 10 is -dx and -dy
        kk(10, x, yLarge, zLarge);

        // set the robot's initial pose
        setPose();
    }

    /**
     * Called during the Autonomous init_loop.
     * Obtains configuration information from the gamepad
     * @param gamepad1 Driver's gamepad
     */
    public static void init_loop(Telemetry telemetry, Gamepad gamepad1) {
        // set the alliance
        if (gamepad1.x) {
            alliance = Alliance.BLUE;
            // update the initial pose
            setPose();
        }
        if (gamepad1.b) {
            alliance = Alliance.RED;
            // update the initial pose
            setPose();
        }

        // set the starting position
        if (gamepad1.dpad_up) {
            startPos = StartPos.BACKSTAGE;
            // update the initial pose
            setPose();
        }
        if (gamepad1.dpad_down) {
            startPos = StartPos.AUDIENCE;
            // update the initial pose
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
