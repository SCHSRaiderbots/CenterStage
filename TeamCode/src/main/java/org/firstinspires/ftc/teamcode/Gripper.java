package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gripper {
    Servo servoGripper;
    public static final double positionGrip = 0.15; //  0.09;
    public static final double positionRelease = 0.5; // 0.01;

    Servo servoGripper2;
    public static final double positionGrip2 = 0.87;
    public static final double positionRelease2 = 0.60;

    public Gripper(HardwareMap hardwareMap) {
        // get the original gripper
        servoGripper = hardwareMap.tryGet(Servo.class, "gripper");
        // get the added gripper
        servoGripper2 = hardwareMap.tryGet(Servo.class, "gripper2");
    }

    public void setPosition(double position) {
        if (servoGripper != null) {
            servoGripper.setPosition(position);
        }
    }

    public void setPosition2(double position) {
        if (servoGripper2 != null) {
            servoGripper2.setPosition(position);
        }
    }

    public double getPosition() {
        if (servoGripper != null) return servoGripper.getPosition();
        else {
            return -1.0;
        }
    }

    public double getPosition2() {
        if (servoGripper2 != null) return servoGripper2.getPosition();
        else {
            return -1.0;
        }
    }

    public void grip() {
        setPosition(positionGrip);
    }

    public void grip2() {
        setPosition2(positionGrip2);
    }

    public void release() {
        setPosition(positionRelease);
    }

    public void release2() {
        setPosition2(positionRelease2);
    }

    public boolean isReal() {
        return servoGripper != null;
    }
    public boolean isReal2() { return servoGripper2 != null; }
}
