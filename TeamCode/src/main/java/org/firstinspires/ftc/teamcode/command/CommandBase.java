package org.firstinspires.ftc.teamcode.command;

/**
 * Make a command-based system similar to the FRC WPIlib.
 * <p>
 *     WPIlib make Command an interface... The interface has the decorations.
 * </p>
 */
public class CommandBase {
    // getName()
    // setName()
    // getSubsystem()
    // setSubsystem()
    // addRequirements
    // getRequirements
    // runsWhenDisabled()
    // getInterruptionBehavior() kCancelSelf, kCancelIncoming

    /**
     * Initialize a command.
     * This method sets everything up for the cammand to start running.
     */
    public void initialize() {
        // initialize the command
    }

    /**
     * Make some progress on completing the command.
     */
    public void execute() {
        // execute the command
    }

    /**
     * Test if the command has finished runningg.
     * @return true if the command is done.
     */
    public boolean isFinished() {
        // test if the command is finished
        return false;
    }

    /**
     * Clean up after the command has finished (or been interrupted).
     * @param interrupted true if the command is being interrupted.
     */
    public void end(boolean interrupted) {
        // clean up the command
    }
}
