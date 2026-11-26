package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name="Pedro: Command", group="Iterative OpMode")
public class TeleOp1 extends RobotOpMode {

    private Follower follower;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void loop() {
        robot.drivetrain.mecanumDrive(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );
    }
}
