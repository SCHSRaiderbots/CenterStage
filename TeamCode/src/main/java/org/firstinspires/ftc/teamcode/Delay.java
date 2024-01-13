package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.command.CommandBase;

public class Delay extends CommandBase {
    /** seconds of delay */
    double m_delay;
    ElapsedTime elapsed;

    public Delay(double seconds) {
        // remember the delay time
        m_delay = seconds;
    }

    @Override
    public void initialize() {
        elapsed = new ElapsedTime();
    }

    @Override
    public boolean isFinished() {
        return elapsed.seconds() > m_delay;
    }
}
