package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Motion.robot;

import androidx.core.math.MathUtils;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.Locale;

/**
 * TeleOp mode for competition.
 */
@TeleOp(name="Teleop Drive", group ="Competition")
public class TeleopDrive extends OpMode {

    // the Vision object
    Vision vision = null;

    // the Drone Launcher
    Drone drone = null;

    // the Wrist
    Wrist wrist = null;

    // the Gripper
    Gripper gripper = null;

    // the Arm
    Arm arm = null;

    // Whether or not to use the IMU
    boolean bIMU = false;

    double armPosition = 0.0;
    // todo this is wrong
    double positionWrist = 0.5;

    // The IMU sensor object
    // BNO055IMU imu = null;
    // there is a new IMU object...
    IMU imu = null;

    /** the LynxModule serial number */
    String strSerialNumber;

    @Override
    public void init() {
        // get the serial number
        // TODO: use serial number to identify robot?
        strSerialNumber = LogDevice.getSerialNumber(hardwareMap);

        // report the LynxModules
        LogDevice.dumpFirmware(hardwareMap);

        // Motion.identifyRobot(hardwareMap);
        robot = RobotId.ROBOT_2023;
        RobotId.identifyRobot(hardwareMap);

        // initialize motion
        Motion.init(hardwareMap);

        // create the vision object
        if (robot == RobotId.ROBOT_2022 || robot == RobotId.ROBOT_2023) {
            vision = new Vision(hardwareMap);

            // we do not use object recognition
            // .enableTfod(true);
        }

        // get the drone launcher and set it to armed.
        drone = new Drone(hardwareMap);

        // get the arm
        arm = new Arm(hardwareMap);

        // get the wrist
        wrist = new Wrist(hardwareMap);

        // get the gripper
        gripper = new Gripper(hardwareMap);

        if (bIMU) {
            // Set up the parameters with which we will use our IMU. Note that integration
            // algorithm here just reports accelerations to the logcat log; it doesn't actually
            // provide positional information.

            /* The next two lines define Hub orientation.
             * The Default Orientation (shown) is when a hub is mounted horizontally with the printed logo pointing UP and the USB port pointing FORWARD.
             *
             * To Do:  EDIT these two lines to match YOUR mounting configuration.
             */
            RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;

            RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
            IMU.Parameters parameters = new IMU.Parameters(orientationOnRobot);

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            imu = hardwareMap.get(IMU.class, "imu");
            imu.initialize(parameters);
        }
    }

    @Override
    public void init_loop() {
        // report the serial number during init
        // this causes an update, so it will flash the display
        // telemetry.addData("Serial Number", strSerialNumber);

        // update the robot pose
        Motion.updateRobotPose();

        // process wrist buttons
        buttonsWrist();

        // process gripper buttons during initialization
        buttonsGripper();
    }

    @Override
    public void start() {
        // report current status

        // Motion.setPoseInches(0,0,0);

        // run using encoder
        Motion.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (vision != null) {
            vision.enableTfod(false);
            vision.enableAprilTags(true);
        }
    }

    @Override
    public void loop() {
        // update the robot pose
        Motion.updateRobotPose();
        Motion.reportPosition(telemetry);

        telemetry.addData("Robot", robot);

        if (vision != null) {
            vision.telemetryAprilTag(telemetry);
        }

        if (bIMU) {
            reportIMU();
        }

        // now process the controls...

        // do some driving was -0.7
        // The minus sign is because stick pushed forward is negative.
        double forward = -1.0 * boost(gamepad1.left_stick_y);
        double turn = 0.4 * (gamepad1.right_stick_x);

        // max tick velocity should 6000 RPM * 28 ticks per rev = 2800
        double rpm = 6000.0;
        double v = (rpm / 60) * Motion.HD_HEX_TICKS_PER_REV;
        Motion.setVelocity(v * (forward+turn), v * (forward-turn));

        if (gamepad1.y) {
            // set the pose
            Motion.setPoseInches(Vision.inchX, Vision.inchY, Vision.degTheta);
        }

        // Button a launches the drone
        drone.setLaunch(gamepad1.a);

        // Hack to find good values for the Drone servo.
        // drone.setAbsolutePosition(gamepad1.left_stick_x);
        // telemetry.addData("Drone", gamepad1.left_stick_x);

        // set the arm position
        double pow = (gamepad1.left_trigger - gamepad1.right_trigger);
        armPosition += 2.0 * pow;
        // arm.setArmAngle(240 * pow);

        if (gamepad1.x) {
            armPosition = -125.0;
        }

        if (gamepad1.y) {
            armPosition = 0.0;
        }

        // clamp the arm position to reasonable values
        armPosition = MathUtils.clamp(armPosition, -220.0, 20.0);
        arm.setArmAngle(armPosition);

        if (armPosition < -180.0) {
            wrist.setPosition(.58);
        } else if (armPosition < -90.0) {
            wrist.setPosition(.50);
        } else {
            buttonsWrist();
        }

        // possibly update the arm offset
        arm.checkHall();

        // process wrist buttons
        // buttonsWrist();

        // process gripper buttons
        buttonsGripper();

        telemetry.addData("Arm angle", arm.getAngle());
        telemetry.addData("Gripper2", gripper.getPosition2());
    }

    /**
     * Square a value retaining the sign
     * @param x value from -1 to 1
     * @return x * abs(x)
     */
    private double boost(double x) {
        return x * Math.abs(x);
    }

    @Override
    public void stop() {
        // turn off tracking
    }

    /**
     * Method to provide consistent wrist controls.
     */
    public void buttonsWrist() {
        if (gamepad2.a) {
            positionWrist += 0.02 * gamepad2.left_stick_y;
            wrist.setPosition(positionWrist);
        } else {
            // wrist.setPosition(gamepad2.right_stick_y);
            if (gamepad2.dpad_up) {positionWrist = 1.0; wrist.setPosition(positionWrist);}
            if (gamepad2.dpad_right) {positionWrist = 0.75; wrist.setPosition(positionWrist);}
            if (gamepad2.dpad_left) {positionWrist = 0.5; wrist.setPosition(positionWrist);}
            if (gamepad2.dpad_down) {positionWrist = 0.25; wrist.setPosition(positionWrist);}
        }
    }

    /**
     * Method to provide consistent gripper controls.
     */
    public void buttonsGripper() {
        // hack to find gripper position
        // gripper.setPosition2(gamepad2.left_trigger);

        if (gamepad1.left_bumper || gamepad2.left_bumper) gripper.release(); else gripper.grip();
        if (gamepad1.right_bumper || gamepad2.right_bumper) gripper.release2(); else gripper.grip2();
    }


    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void reportIMU() {
        // Retrieve Rotational Angles and Velocities
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

        telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
        telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
        telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));
        telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
        telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
        telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", angularVelocity.yRotationRate);
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}