package org.firstinspires.ftc.teamcode.command;

public class SequentialCommandGroup extends CommandBase {
    int iCommand = 0;
    CommandBase[] aCommands;

    /**
     * Execute a sequence of commands
     */
    public SequentialCommandGroup(CommandBase ... commands) {
        // remember all the commands
        aCommands = commands;

        // fake that we are done
        iCommand = aCommands.length;
    }

    @Override
    public void initialize() {
        // start with the first command
        iCommand = 0;

        // and initialize it
        if (iCommand < aCommands.length) {
            aCommands[iCommand].initialize();
        }
    }

    @Override
    public void execute() {
        while (iCommand < aCommands.length) {
            // make some progress on the current step
            aCommands[iCommand].execute();

            if (aCommands[iCommand].isFinished()) {
                // end this command
                aCommands[iCommand].end(false);

                // advance to the next command
                iCommand++;
                if (iCommand < aCommands.length) {
                    aCommands[iCommand].initialize();
                }
            } else {
                // more to do on this command next time around
                break;
            }
        }
    }

    @Override
    public boolean isFinished() {
        // we are finished if we have run out of commands
        return iCommand >= aCommands.length;
    }

    @Override
    public void end(boolean interrupted) {
        if (iCommand < aCommands.length) {
            // terminate the current command
            aCommands[iCommand].end(interrupted);
        }

        // do not run any more commands
        iCommand = aCommands.length;
    }
}
