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

    public static final float inchTile = 22.75f;

    // figure out positions of the April Tags

    // April Tags 1-6 are on the Backdrops
    public static final float xBackdrop = 2.0f * inchTile + 12.5f;
    public static final float yBackdropCenter = 1.5f * inchTile;
    // tags 1-6 are 6 inches apart
    public static final float yBackdropOffset = 6.0f;
    public static final float zBackdrop = 0.0f;

    // 1 is at y+6 (FTC says 60.3, 41.4, 4.0)
    public static final OpenGLMatrix atag1 = zx(1,xBackdrop, yBackdropCenter+yBackdropOffset, zBackdrop);
    // 2 is at y  (FTC says 60.3, 35.4, 4.0)
    public static final OpenGLMatrix atag2 = zx(2, xBackdrop, yBackdropCenter, zBackdrop);
    // 3 is at y-6 (FTC says 60.3, 29.4, 4.0)
    public static final OpenGLMatrix atag3 = zx(3, xBackdrop,yBackdropCenter-yBackdropOffset, zBackdrop);
    // 4 is at -y+6
    public static final OpenGLMatrix atag4 = zx(4, xBackdrop,-yBackdropCenter+yBackdropOffset, zBackdrop);
    // 5 is at -y
    public static final OpenGLMatrix atag5 = zx(5, xBackdrop, -yBackdropCenter, zBackdrop);
    // 6 is at -y-6
    public static final OpenGLMatrix atag6 = zx(6, xBackdrop,-yBackdropCenter-yBackdropOffset, zBackdrop);

    // April Tags 7-10 are on the audience wall

    // The small (2 inch by 2 inch) 8 and 9 centers are 4 inches above the deck
    // The large (5 inch by 5 inch) are dx = 5.5 inches and dz = 1.5 inch

    // The dy distance can be calculated. Tag edges are 2 inches apart.
    //   So dy = 2.0 + 0.5 * 2 + 0.5 * 5 = 5.5 inches
    // the middle stack is at the 1 tile junction; others are offset by 11 inches
    public static final float ySmall = 1.0f * inchTile + 11.0f;
    public static final float yLarge = ySmall + 5.5f;

    // The dz can be calculated because the bottoms of the April Tags are aligned.
    //   thus center of 5 inch is +2.5, center of 2 inch is +1.0; diff is 1.5 inch
    // the small (2 inch by 2 inch) April Tags are 4.0 inches above the deck
    public static final float zSmall = 4.0f;
    // the large (5 inch by 5 inch) April Tags are 4.0 + 1.5 inches above the deck
    public static final float zLarge = 4.0f + 1.5f;

    // y value is constant
    public static final float xTag = -3.0f * inchTile - 0.5f;

    // now position the tags...
    // 7 is a +dx and +dy (FTC says -70.3, -40.6, 5.5)
    public static final OpenGLMatrix atag7 = kk(7, xTag, -yLarge, zLarge);
    // 8 is a 1 tile + 11 inches (FTC says -70.3, -35.1, 4.0)
    public static final OpenGLMatrix atag8 = kk(8, xTag, -ySmall, zSmall);
    // 9 is at -1 tile - 11 inches
    public static final OpenGLMatrix atag9 = kk(9, xTag, +ySmall, zSmall);
    // 10 is -dx and -dy
    public static final OpenGLMatrix atag10 = kk(10, xTag, +yLarge, zLarge);

    private static OpenGLMatrix zx(int tag, float dx, float dy, float dz) {
        // rotate by 60 around the x to make the image almost vertical
        float rx = 60.0f;
        // no rotation about the y axis
        float ry = 0.0f;
        // rotate about z axis
        float rz = -90.0f;
        return OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz))
                .multiplied(OpenGLMatrix.translation(0.0f, 4.875f, 0.0f));
    }

    private static OpenGLMatrix kk(int tag, float dx, float dy, float dz) {
        // rotate by 90 around the x to make the image vertical
        float rx = 90.0f;
        // no rotation about the y axis
        float ry = 0.0f;
        // rotate 90 so picture z is pointing into arena.
        float rz = 90.0f;
        // mumble
        return OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz));
    }

    public static void init() {



        // set the robot's initial pose
        setPose();
    }

    /**
     * Called during the Autonomous init_loop.
     * Obtains configuration information from the gamepad.
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
     * Set the robot pose based on the alliance and the starting position.
     * <p> Blue alliance is A2 or A4 (tile y axis uses 1-based numbers) </p>
     * <p> Red alliance is F2 or F4 </p>
     */
    public static void setPose() {
        // TODO: should be a property of the robot
        double robotBackDistance = 7.75;

        // set the starting tile to 2 or 4
        // TODO: really 1.5 tiles
        double dx = 36.0;
        double x = (startPos == StartPos.AUDIENCE)? -dx : +dx;

        /// set the starting tile to A or F; direction to plus or minus 90 degrees
        // TODO: assumes alliance wall is 70.75 (x2 = 141.5)
        double fy = 70.75 - robotBackDistance;
        double y = (alliance == Alliance.RED)? -fy : +fy;
        double ang = (alliance == Alliance.RED)? +90.0 : -90.0;

        Motion.setPoseInches(x, y, ang);
    }

}
