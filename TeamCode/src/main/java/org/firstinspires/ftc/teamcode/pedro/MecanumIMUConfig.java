
package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection;

public class MecanumIMUConfig {
    public String frontLeftName = "leftFront";
    public String backLeftName = "backLeft";
    public String frontRightName = "rightFront";
    public String backRightName = "backRight";
    public String imuName = "imu";

    public LogoFacingDirection logoFacingDirection = LogoFacingDirection.DOWN;
    public UsbFacingDirection usbFacingDirection = UsbFacingDirection.DOWN;

    // Calibrate by pushing 48 in forward and 48 in left
    public double forwardTicksToInches = 0.00295;
    public double strafeTicksToInches = 0.00335;
}

