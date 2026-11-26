package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.drivetrain.Drivetrain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.*;

public class Robot {
    public final HardwareMap hardwareMap;
    public final Telemetry telemetry;
    public final DriveTrain drivetrain;
    public final Intake intake;

    public final Windmill windmill;
    public final Pusher pusher;
    public final Launcher launcher;
    //public final Blocker blocker;
    //public final Turret turret;
    //public final TapeSensor tapeSensor;

    public Robot(OpMode opMode) {
        hardwareMap = opMode.hardwareMap;
        telemetry = new MultipleTelemetry(
                opMode.telemetry,
                FtcDashboard.getInstance().getTelemetry(),
                PanelsTelemetry.INSTANCE.getFtcTelemetry()
        );

        drivetrain = new DriveTrain(this);
        intake = new Intake(this);
        pusher = new Pusher(this);
        launcher = new Launcher(this);
        //blocker = new Blocker(this);
        windmill = new Windmill(this);
        //tapeSensor = new TapeSensor(this);
    }
}
