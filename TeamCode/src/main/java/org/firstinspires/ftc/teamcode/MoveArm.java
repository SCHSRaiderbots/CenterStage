package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.command.CommandBase;

public class MoveArm extends CommandBase {
    Arm m_arm;
    double m_angle;

    public MoveArm(double angle, Arm arm) {
        // remember the arguments
        m_angle = angle;
        m_arm = arm;
    }

    @Override
    public void initialize() {
        // start the arm moving
        m_arm.setArmAngle(m_angle);
    }

    @Override
    public void execute() {
        // nothing to do
    }

    @Override
    public boolean isFinished() {
        return m_arm.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        // nothing to do
    }
}
