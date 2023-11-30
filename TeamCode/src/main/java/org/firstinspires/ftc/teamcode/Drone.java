package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Drone {
    Servo servoDrone;
    static final double posArmed = 0.1;
    static final double posFired = 0.4;

    public Drone(HardwareMap hardwareMap) {
        // get the servo
        servoDrone = hardwareMap.get(Servo.class, "servoDrone");

        servoDrone.setPosition(posArmed);
    }

    public void setArmed(boolean isArmed) {
        servoDrone.setPosition((isArmed)? posArmed : posFired);
    }
}
