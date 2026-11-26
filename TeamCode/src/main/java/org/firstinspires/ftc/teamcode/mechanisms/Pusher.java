package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class Pusher {

    enum Mode {
        UP,
        DOWN
    }

    private Servo pusher = null;

    public static double upPosition = 0.0;
    public static double downPosition = 0.25;

    private Pusher.Mode mode = Pusher.Mode.UP;

    private final Telemetry telemetry;

    public Pusher(Robot robot){
        pusher = robot.hardwareMap.get(Servo.class, "pusher");

        telemetry = robot.telemetry;
    }

    public Command on() {
        return instant(() -> mode = Pusher.Mode.UP).requiring(pusher);
    }

    public Command off() {
        return instant(() -> mode = Mode.DOWN).requiring(pusher);
    }

    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case UP:
                    pusher.setPosition(upPosition);
                    break;
                case DOWN:
                    pusher.setPosition(downPosition);
                    break;
            }

            telemetry.addData("Pusher Is At Position", pusher.getPosition());

        });
    }
}
