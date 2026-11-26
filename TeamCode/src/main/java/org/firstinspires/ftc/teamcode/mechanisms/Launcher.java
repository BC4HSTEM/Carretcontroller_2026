package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class Launcher {

    enum Mode {
        ON,
        OFF
    }
    private DcMotorEx launcher = null;

    public static int LAUNCHER_TARGET_VELOCITY = 1150;
    public static int LAUNCHER_MIN_VELOCITY = 950;

    private boolean on = false;
    private Launcher.Mode mode = Intake.Mode.OFF;

    private final Telemetry telemetry;

    public Launcher(Robot robot){

        launcher = robot.hardwareMap.get(DcMotorEx.class, "launcher");

        telemetry = robot.telemetry;
    }

    public void turnOn() {
        on = true;
    }

    public void turnOff() {
        on = false;
    }

    public Command on() {
        return instant(() -> mode = Launcher.Mode.ON).requiring(launcher);
    }

    public Command off() {
        return instant(() -> mode = Launcher.Mode.OFF).requiring(launcher);
    }

    public Command toggle() {
        return conditional(() -> mode == Launcher.Mode.OFF, on(), off());
    }



    public double getVelocity() {
        return launcher.getVelocity();
    }
    public double getMinVelocity() {
        return LAUNCHER_MIN_VELOCITY;
    }

    public boolean atTargetVelocity(){
        return getVelocity() > getMinVelocity();
    }

    private void setVelocity(double velocity) {
        launcher.setVelocity(velocity);

    }

    public Command periodic() {
        return infinite(() -> {
            if (on) {
                setVelocity(LAUNCHER_TARGET_VELOCITY);
            } else {
                setVelocity(0);
            }

            telemetry.addData("Launcher Velocity", getVelocity());
            telemetry.addData("Launcher Target", LAUNCHER_TARGET_VELOCITY);

        });
    }
}
