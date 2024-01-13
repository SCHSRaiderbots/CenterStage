package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.CenterStage.tileX;
import static org.firstinspires.ftc.teamcode.CenterStage.tileXR;
import static org.firstinspires.ftc.teamcode.CenterStage.tileYR;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.command.CommandBase;
import org.firstinspires.ftc.teamcode.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.command.SequentialCommandGroup;

@Autonomous(name="Auto Wave", group="testing")
public class AutoWave extends OpMode {
    CommandBase command;
    Vision vision;
    Arm arm;
    Wrist wrist;
    Gripper gripper;

    /** result of object detection */
    int hit = 1;

    @Override
    public void init() {
        // identify the robot
        RobotId.identifyRobot(hardwareMap);
        Motion.init(hardwareMap);

        vision = new Vision(hardwareMap);
        // enable detection
        vision.enableTfod(true);

        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);
        gripper = new Gripper(hardwareMap);

        CenterStage.init();
    }

    @Override
    public void init_loop() {
        // figure our position
        Motion.updateRobotPose();

        // collect the starting information
        CenterStage.init_loop(telemetry, gamepad1);

        // report the hit
        hit = vision.objectNumber();
        telemetry.addData("hit", hit);

        // report the starting position
        telemetry.addData("pose", "%8.2f %8.2f %8.2f", Motion.xPoseInches, Motion.yPoseInches, Motion.thetaPoseDegrees);

    }

    @Override
    public void start() {
        // remember the hit
        hit = vision.objectNumber();

        // make the command
        command = new SequentialCommandGroup(
                new Delay(0.5),
                // drive forward
                new DriveForward( tileX(2, 6.0)),
                new DriveTurnToward(tileXR(2.5), tileYR(-0.5)),
                new DriveTo(tileX(2.5), tileYR(-0.5)),
                new MoveArm(0, arm)
        );

        command.initialize();
        command.execute();

        // turn off detection
        vision.enableTfod(false);
    }

    @Override
    public void loop() {
        // report the hit
        telemetry.addData("hit", hit);

        // figure our position
        Motion.updateRobotPose();

        if (!command.isFinished()) {
            command.execute();
        }
    }

    @Override
    public void stop() {
        command.end(true);
    }
}
