package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class Windmill {

    enum Mode {
        ON,
        OFF
    }
    private CRServo windmill = null;

    public static double onPower = 1;
    public static double offPower = 0;

    private Windmill.Mode mode = Windmill.Mode.OFF;

    private final Telemetry telemetry;

    public Windmill(Robot robot) {

        windmill = robot.hardwareMap.get(CRServo.class, "windMill");
        windmill.setPower(0);

        telemetry = robot.telemetry;
    }

    public Command on() {
        return instant(() -> mode = Windmill.Mode.ON).requiring(windmill);
    }

    public Command off() {
        return instant(() -> mode = Windmill.Mode.OFF).requiring(windmill);
    }

    public Command toggle() {
        return conditional(() -> mode == Windmill.Mode.OFF, on(), off());
    }

    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case ON:
                    windmill.setPower(onPower);
                    break;
                case OFF:
                    windmill.setPower(offPower);
                    break;
            }

            telemetry.addData("Windmill Is On", windmill.getPower());

        });
    }
}
