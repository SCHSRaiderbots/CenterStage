package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.command.CommandBase;
import org.firstinspires.ftc.teamcode.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.command.SequentialCommandGroup;

@Autonomous(name="Auto Wave", group="testing")

public class AutoWave extends OpMode {
    CommandBase command;
    Arm arm;

    @Override
    public void init() {
        // identify the robot
        RobotId.identifyRobot(hardwareMap);
        Motion.init(hardwareMap);

        arm = new Arm(hardwareMap);

        command = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new MoveArm(30, arm),
                        new DriveTo(12.0, 0.0)
                ),
                new MoveArm(-30, arm),
                new ParallelCommandGroup(
                        new DriveTo(-12, 0.0),
                        new MoveArm(0, arm)
                )
        );

        CenterStage.init();
    }

    @Override
    public void init_loop() {
        // figure our position
        Motion.updateRobotPose();

        // collect the starting information
        CenterStage.init_loop(telemetry, gamepad1);
    }

    @Override
    public void start() {
        command.initialize();
        command.execute();
    }

    @Override
    public void loop() {
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
