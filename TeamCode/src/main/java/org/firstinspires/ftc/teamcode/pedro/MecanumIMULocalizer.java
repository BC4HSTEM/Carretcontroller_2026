package org.firstinspires.ftc.teamcode.pedro;

import static org.firstinspires.ftc.teamcode.pedro.Constants.drivetrainConfig;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.localization.MotionState;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.List;

public class MecanumIMULocalizer implements Localizer {
    private final DcMotorEx fl, fr, bl, br;
    private final IMU imu;
    private final List<LynxModule> allHubs;
    private final MecanumIMUConfig config;

    private Pose currentPose = new Pose(0, 0, 0);
    private Vector2D currentVelocity = Vector2D.cartesian(0, 0);
    private double currentAngularVelocity = 0.0;

    private double lastFL, lastFR, lastBL, lastBR;
    private double lastRawHeading;
    private long lastTimeNs;

    public MecanumIMULocalizer(HardwareMap h, MecanumIMUConfig config) {
        this.config = config;

        allHubs = h.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        fl = h.get(DcMotorEx.class, config.frontLeftName);
        fr = h.get(DcMotorEx.class, config.frontRightName);
        bl = h.get(DcMotorEx.class, config.backLeftName);
        br = h.get(DcMotorEx.class, config.backRightName);

        imu = h.get(IMU.class, config.imuName);
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                config.logoFacingDirection,
                config.usbFacingDirection
        )));

        lastFL = fl.getCurrentPosition();
        lastFR = fr.getCurrentPosition();
        lastBL = bl.getCurrentPosition();
        lastBR = br.getCurrentPosition();
        lastRawHeading = getRawHeading();
        lastTimeNs = System.nanoTime();
    }

    @Override
    public void update() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        long now = System.nanoTime();
        double dt = (now - lastTimeNs) / 1e9;
        lastTimeNs = now;

        double currFL = fl.getCurrentPosition();
        double currFR = fr.getCurrentPosition();
        double currBL = bl.getCurrentPosition();
        double currBR = br.getCurrentPosition();

        double dFL = currFL - lastFL;
        double dFR = currFR - lastFR;
        double dBL = currBL - lastBL;
        double dBR = currBR - lastBR;

        lastFL = currFL;
        lastFR = currFR;
        lastBL = currBL;
        lastBR = currBR;

        // Robot-frame displacement (+X forward, +Y left)
        double dxRobot = ((dFL + dFR + dBL + dBR) / 4.0) * config.forwardTicksToInches;
        double dyRobot = ((dFR + dBL - dFL - dBR) / 4.0) * config.strafeTicksToInches;

        double rawHeading = getRawHeading();
        double dTheta = angleWrap(rawHeading - lastRawHeading);
        lastRawHeading = rawHeading;

        // Pose exponential integration
        double sinTerm, cosTerm;
        if (Math.abs(dTheta) < 1e-6) {
            sinTerm = 1.0 - (dTheta * dTheta) / 6.0;
            cosTerm = dTheta / 2.0;
        } else {
            sinTerm = Math.sin(dTheta) / dTheta;

            cosTerm = (1.0 - Math.cos(dTheta)) / dTheta;
        }

        double dxLocal = sinTerm * dxRobot - cosTerm * dyRobot;
        double dyLocal = cosTerm * dxRobot + sinTerm * dyRobot;

        double heading = currentPose.heading();
        double globalDx = dxLocal * Math.cos(heading) - dyLocal * Math.sin(heading);
        double globalDy = dxLocal * Math.sin(heading) + dyLocal * Math.cos(heading);
        double newHeading = normalizeRadians(heading + dTheta);

        currentPose = new Pose(
                currentPose.x() + globalDx,
                currentPose.y() + globalDy,
                newHeading
        );

        if (dt > 0) {
            currentVelocity = Vector2D.cartesian(globalDx / dt, globalDy / dt);
            currentAngularVelocity = dTheta / dt;
        }
    }

    @Override
    public void reset() {

    }


    @Override
    public void setPose(Pose pose) {
        currentPose = pose;
        lastRawHeading = getRawHeading();
    }

    @Override
    public MotionState state() {
        return null;
    }


    private double getRawHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    private static double angleWrap(double angle) {
        while (angle > Math.PI) angle -= 2.0 * Math.PI;
        while (angle < -Math.PI) angle += 2.0 * Math.PI;
        return angle;
    }

    private static double normalizeRadians(double angle) {
        double twoPi = 2.0 * Math.PI;
        angle %= twoPi;
        return angle < 0 ? angle + twoPi : angle;
    }

    // Inside Constants.java
    public static MecanumIMUConfig localizerConfig = new MecanumIMUConfig();



}
