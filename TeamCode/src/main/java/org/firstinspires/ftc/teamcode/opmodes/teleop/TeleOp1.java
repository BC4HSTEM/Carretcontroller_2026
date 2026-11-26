package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name="Pedro: Command")
@Config
public class TeleOp1 extends RobotOpMode {


    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        robot.drivetrain.mecanumDrive(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x

        );

        if (gamepad1.leftTriggerWasPressed()) robot.intake.off().schedule();
        if (gamepad1.leftTriggerWasReleased()) robot.intake.on().schedule();
        if (gamepad1.rightTriggerWasPressed()) robot.intake.reverse().schedule();


        if(gamepad1.rightBumperWasPressed()){
            robot.launcher.on();
            if(robot.launcher.atTargetVelocity()){
                robot.feeder.on().schedule();
            }
        }
        else {
            robot.feeder.off().schedule();
        }

        if(gamepad1.dpadUpWasPressed()){
            robot.pusher.up();
        }
        if(gamepad1.dpadDownWasPressed()){
            robot.pusher.down();
        }





    }
}
