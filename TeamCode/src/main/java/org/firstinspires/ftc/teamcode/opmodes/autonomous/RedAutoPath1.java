package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;
@Autonomous(name = "Red Auto 1")
public class RedAutoPath1 extends RobotOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose path1 = poseFactory.of(56.3394, 41.8838, 90);
    private final Pose point2Start = poseFactory.of(56.3394, 41.8838, 90);
    private final Pose point2 = poseFactory.of(14.8203, 46.3181, 180);

    public static double shotsWaitTime = 4000; //ms

    public Command autoRoutine() {
        robot.launcher.on().schedule();
        return sequential(

                //make sure pusher is up
                robot.pusher.up(),
                //path 1
                robot.drivetrain.follow(path1()),


                waitUntil(robot.launcher::atTargetVelocity).then(robot.feeder.on()),

                waitMs(shotsWaitTime), //wait until all 4 shots fired
                //path 2
                robot.drivetrain.follow(path2()),
                robot.launcher.off()


        );
    }

    @Override
    public void init() {
        super.init();

        Scheduler.reset();

        robot.drivetrain.setPose(start);

        schedule(autoRoutine());
    }

    @Override
    public void start() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("heading", follower.pose().heading());

        if (follower.currentPath() != null) {
            telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
            telemetry.addData("Path number", follower.pathIndex());
        }

        telemetry.update();
    }

    public Path path1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);}
}
