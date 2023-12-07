package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * This autonomous routine just sits in one place
 */
@Autonomous(name="Sit Still", group="competition")
public class AutoSit extends OpMode {
    Vision vision;

    @Override
    public void init() {
        // identify the robot
        RobotId.identifyRobot(hardwareMap);
        Motion.init(hardwareMap);

        vision = new Vision(hardwareMap);

        // enable detection
        vision.enableTfod(true);

        CenterStage.init();
    }

    @Override
    public void init_loop() {
        // figure our position
        Motion.updateRobotPose();

        // collect the starting information
        CenterStage.init_loop(telemetry, gamepad1);

        // look for the pixel
        vision.telemetryTfod(telemetry);
    }

    @Override
    public void start() {
        vision.enableTfod(false);
        vision.enableAprilTags(true);
    }

    @Override
    public void loop() {
        // figure our position
        Motion.updateRobotPose();

        vision.telemetryAprilTag(telemetry);

    }

    @Override
    public void stop() {

    }
}
