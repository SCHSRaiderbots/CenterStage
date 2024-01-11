package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.command.CommandBase;

public class DriveTurnDirection extends CommandBase {
    double m_x;
    double m_y;

    public DriveTurnDirection(double x, double y) {
        // remember the direction
        m_x = x;
        m_y = y;
    }

    @Override
    public void initialize() {
        // TODO: This is wrong...
        Motion.headingInches(m_x, m_y);
    }

    @Override
    public void execute() {
        // nothing to do
    }

    @Override
    public boolean isFinished() {
        return Motion.finished();
    }

    @Override
    public void end(boolean interrupted) {
        // nothing to do
    }
}
