package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.command.CommandBase;

public class DriveTo extends CommandBase {
    private final double m_x;
    private final double m_y;

    public DriveTo(double x, double y) {
        // remember the arguments
        m_x = x;
        m_y = y;
    }

    @Override
    public void initialize() {
        // move to position
        Motion.moveInches(m_x);
    }

    @Override
    public void execute() {
        // motor controller will do the work
    }

    @Override
    public boolean isFinished() {
        return Motion.finished();
    }

    @Override
    public void end(boolean interrupted) {

    }
}
