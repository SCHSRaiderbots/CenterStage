package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.command.CommandBase;

public class DriveForward extends CommandBase {
    /** distance to travel (inches) */
    double m_distance;

    /**
     * Drive forward a specific distance
     * @param distance distance in inches
     */
    public DriveForward(double distance) {
        m_distance = distance;
    }

    @Override
    public void initialize() {
        // TODO: set the power?
        // Move the distance
        Motion.moveInches(m_distance);
    }

    @Override
    public boolean isFinished() {
        return Motion.finished();
    }
}
