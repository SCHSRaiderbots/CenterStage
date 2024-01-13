package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Arm {
    private DcMotorEx motorArm;

    // the hall-effect sensor can be viewed a touch sensor
    private TouchSensor hall;
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
            // motorArm.setPower(0.5);

            // get the hall sensor
            hall = hardwareMap.get(TouchSensor.class, "hall");
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

    public double getAngle() {
        return (motorArm.getCurrentPosition() - positionOffset) / ((288.0 / 360.0) * (125.0 / 30.0));
    }

    /**
     * Set the motor power directly...
     * @param power in domain [-1,1]
     */
    public void setPower(double power) {
        if (motorArm != null) {
            motorArm.setPower(power);
        }
    }

    /**
     * Report whether the arm has finished moving
     * @return true if the arm has reached its position
     */
    public boolean isFinished() {
        // query the motor controller
        return !motorArm.isBusy();
    }

    /**
     * Report whether the arm is using real hardware.
     * @return true if the hardware exists.
     */
    public boolean isReal() {
        // test if the arm motor exists.
        return motorArm != null;
    }

    public void checkHall() {
        if (hall.isPressed()) {
            // detect zero angle...
            // say the current zero is 1000
            // say we zero at 100
            // so the new target position should be 900 less
            int target = motorArm.getTargetPosition();
            int positionOffsetNew = motorArm.getCurrentPosition();
            int delta = positionOffsetNew - positionOffset;

            // update the target position
            motorArm.setTargetPosition(target + delta);

            // remember the new offset
            positionOffset = positionOffsetNew;
        }
    }
}
