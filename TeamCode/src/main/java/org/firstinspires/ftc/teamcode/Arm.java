package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Arm {
    private DcMotorEx motorArm;
    private int positionOffset = 0;

    public Arm(HardwareMap hardwareMap) {
        // get the motor
        motorArm = hardwareMap.tryGet(DcMotorEx.class, "motorArm");

        if (motorArm != null) {
            // Set PIDF coefficients
            // 500 oscillates
            // 200 has some overshoot
            // even at 10, I have overshoot
            // AND arm jitters, so is other PIDF mode also in play?
            PIDFCoefficients pidf = new PIDFCoefficients(10, 0.0, 0.0, 0.0, MotorControlAlgorithm.PIDF);
            motorArm.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidf);

            // set PID tolerance
            motorArm.setTargetPositionTolerance(10);

            // set to the current position
            positionOffset = motorArm.getCurrentPosition();
            setArmAngle(0);

            // use the encoder
            motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // motorArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // set power
            motorArm.setPower(1.0);
        }
    }

    /**
     * Set the arm to a specific position.
     * @param degrees
     */
    public void setArmAngle(double degrees) {
        // convert degrees to ticks
        // 288 ticks per rotation
        // gear ratio is 30:125
        double tickPosition = degrees * (288.0 / 360.0) * (125.0 / 30.0);
        if (motorArm != null) {
            int ticks = (int)tickPosition + positionOffset;

            motorArm.setTargetPosition(ticks);
        }
    }

    public void setPower(double power) {
        if (motorArm != null) {
            motorArm.setPower(power);
        }
    }

    public boolean isFinished() {
        return !motorArm.isBusy();
    }

    public boolean isReal() {
        return motorArm != null;
    }
}
