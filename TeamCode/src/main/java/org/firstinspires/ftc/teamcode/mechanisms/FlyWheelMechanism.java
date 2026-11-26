package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlyWheelMechanism {

    private DcMotorEx launcher = null;
    private CRServo gate = null;

    private ElapsedTime stateTimer = new ElapsedTime();

    private Gamepad gamepad1;

    private Telemetry telemetry;

    private enum FlyWheelState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        RESET_GATE
    }

    private FlyWheelState flyWheelState;

    //--------------------------- WINDMILL SERVO CONSTANTS ------------------------//

    private int GATE_STOP_POWER = 0;
    private int GATE_SPIN_POWER = 1;

    private double GATE_SPIN_TIME = 1; //time is takes to push a pollen in position for launch



    //--------------------------- FLYWHEEL MOTOR CONSTANTS ------------------------//

    public static double LAUNCHER_TARGET_RPM= 1070;
    public static double LAUNCHER_MIN_RPM = 870;
    private double shotsRemaining = 0;
    private double flywheelVelocity = 0;

    private double LAUNCHER_MAX_SPIN_UP_TIME = 2;

    private void init (HardwareMap hardwareMap, Gamepad global_gamepad1, Telemetry global_telemetry){

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        gate = hardwareMap.get(CRServo.class, "windMill");

        gate.setDirection(CRServo.Direction.REVERSE);

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        gamepad1 = global_gamepad1;
        telemetry = global_telemetry;

        telemetry.addData("Status", "Initialized");

        //Tune PIDF

        flyWheelState = FlyWheelState.IDLE;
        gate.setPower(0);


    }

    private void update(){

        switch (flyWheelState){
            case IDLE:
                if(shotsRemaining > 0){
                    gate.setPower(0);
                    launcher.setVelocity(LAUNCHER_TARGET_RPM);

                    stateTimer.reset();

                    flyWheelState = FlyWheelState.SPIN_UP;
                }

                break;

            case SPIN_UP:

                if(launcher.getVelocity() > LAUNCHER_MIN_RPM || stateTimer.seconds() > LAUNCHER_MAX_SPIN_UP_TIME){
                    gate.setPower(GATE_SPIN_POWER);
                    stateTimer.reset();

                    flyWheelState = FlyWheelState.LAUNCH;

                }

                break;

            case LAUNCH:
                if(stateTimer.seconds() > GATE_SPIN_TIME){
                    shotsRemaining--;
                    gate.setPower(GATE_STOP_POWER);

                    stateTimer.reset();

                    flyWheelState = FlyWheelState.RESET_GATE;
                }

                break;

            case RESET_GATE:
                if(shotsRemaining > 0){
                    stateTimer.reset();
                    flyWheelState = FlyWheelState.SPIN_UP;
                }
                else {
                    launcher.setVelocity(0);
                    flyWheelState = FlyWheelState.IDLE;
                }

                break;


        }
    }

    public void fireShots(int numberOfShots){
        if (flyWheelState == FlyWheelState.IDLE){
            shotsRemaining = numberOfShots;
        }
    }

    public boolean isBusy(){
        return flyWheelState != FlyWheelState.IDLE;
    }





}
