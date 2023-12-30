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
            PIDFCoefficients pidf = new PIDFCoefficients(20, 0.0, 0.0, 0.0, MotorControlAlgorithm.PIDF);
            motorArm.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidf);

            // set to the current position
            positionOffset = motorArm.getCurrentPosition();
            setArmAngle(0);

            // set PID tolerance
            motorArm.setTargetPositionTolerance(10);

            // use the encoder
            motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motorArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // set power
            // motorArm.setPower(0.1);
        }
    }

    public void setArmAngle(double degrees) {
        if (motorArm != null) {
            int ticks = (int)degrees + positionOffset;

            // motorArm.setTargetPosition(ticks);

            motorArm.setPower(degrees/100.0);
        }
    }

    public void setPower(double power) {
        if (motorArm != null) {
            motorArm.setPower(power);
        }
    }

    public boolean isReal() {
        return motorArm != null;
    }
}
