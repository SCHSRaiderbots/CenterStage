package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Wrist {
    Servo servoWrist;

    public Wrist(HardwareMap hardwareMap) {
        servoWrist = hardwareMap.tryGet(Servo.class, "wrist");
    }

    public void setPosition(double position) {
        if (servoWrist != null) {
            servoWrist.setPosition(position);
        }
    }

    public double getPosition() {
        if (servoWrist != null) {
            return servoWrist.getPosition();
        } else {
            return -1.0;
        }
    }

    public boolean isReal() {
        return servoWrist != null;
    }
}
