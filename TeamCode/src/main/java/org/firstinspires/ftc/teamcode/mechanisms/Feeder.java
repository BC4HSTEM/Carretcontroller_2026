package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class Feeder {

    enum Mode {
        ON,
        OFF
    }
    private CRServo feeder = null;

    public static double onPower = 1;
    public static double offPower = 0;

    public static double numberOfShots = 4;

    private double getNumberOfShotsRemaining = 0;

    private Feeder.Mode mode = Feeder.Mode.OFF;

    private final Telemetry telemetry;

    public Feeder(Robot robot) {

        feeder = robot.hardwareMap.get(CRServo.class, "windMill");
        feeder.setPower(0);

        telemetry = robot.telemetry;
    }

    public double setGetNumberOfShotsRemaining(){
        return getNumberOfShotsRemaining;
    }

    public Command on() {
        return instant(() -> mode = Feeder.Mode.ON).requiring(feeder);
    }

    public Command off() {
        return instant(() -> mode = Feeder.Mode.OFF).requiring(feeder);
    }

    public Command toggle() {
        return conditional(() -> mode == Feeder.Mode.OFF, on(), off());
    }

    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case ON:
                    feeder.setPower(onPower);
                    break;
                case OFF:
                    feeder.setPower(offPower);
                    break;
            }

            telemetry.addData("Windmill Is On?", feeder.getPower());

        });
    }
}
