package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gripper {
    public static final double positionGrip = 0.09;
    public static final double positionRelease = 0.01;
    Servo servoGripper;

    public Gripper(HardwareMap hardwareMap) {
        servoGripper = hardwareMap.tryGet(Servo.class, "gripper");
    }

    public void setPosition(double position) {
        if (servoGripper != null) {
            servoGripper.setPosition(position);
        }
    }

    public void grip() {
        setPosition(positionGrip);
    }

    public void release() {
        setPosition(positionRelease);
    }

    public boolean isReal() {
        return servoGripper != null;
    }
}
