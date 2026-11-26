package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;

public class DriveTrain {

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    public final Follower follower;
    private final Telemetry telemetry;

    public DriveTrain(Robot robot) {
        follower = Constants.createFollower(robot.hardwareMap);

        frontLeftDrive = robot.hardwareMap.get(DcMotor.class, "frontLeft");
        backLeftDrive = robot.hardwareMap.get(DcMotor.class, "backLeft");
        frontRightDrive = robot.hardwareMap.get(DcMotor.class, "frontRight");
        backRightDrive = robot.hardwareMap.get(DcMotor.class, "backRight");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry = robot.telemetry;
    }

    public void mecanumDrive(double forward, double strafe, double turn){

        follower.manual(
                forward,
                strafe,
                turn
        );
        follower.update();
    }

    public Command periodic() {
        return infinite(() -> {
            follower.update();

            telemetry.addData("Current X", follower.pose().x());
            telemetry.addData("Current Y", follower.pose().y());
            telemetry.addData("Current Heading", Math.toDegrees(follower.pose().heading()));

        });
    }
}
