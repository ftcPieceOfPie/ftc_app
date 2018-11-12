package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helper.MotorHelper;
import org.firstinspires.ftc.teamcode.helper.SensorHelper;

public class Sampling {

    public void forward (DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, MotorHelper pie, SensorHelper sensorHelper,
                         Telemetry telemetry, ColorSensor middleColor){

        double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 17.5;
        double targetPositionRight = 17.5;
        double timeoutS = 5;
        pie.movingWithEncoders(frontRight,frontLeft,backRight,backLeft,powerRight,powerLeft,targetPositionRight,
                targetPositionLeft,timeoutS,telemetry);
        // Detect whether center block is yellow
        /*boolean yellow = sensorHelper.isYellow(middleColor);
        if(yellow){
            powerRight = 0.25;
            powerLeft = 0.25;
            targetPositionLeft = 4;
            targetPositionRight = 4;
            timeoutS = 5;
            pie.movingWithEncoders(frontRight,frontLeft,backRight,backLeft,powerRight,powerLeft,targetPositionRight,
                    targetPositionLeft,timeoutS,telemetry);
            telemetry.addData("Travel Forward: " , "yes");
            powerRight = -0.25;
            powerLeft = -0.25;
            targetPositionLeft = -4;
            targetPositionRight = -4;
            timeoutS = 5;
            pie.movingWithEncoders(frontRight,frontLeft,backRight,backLeft,powerRight,powerLeft,targetPositionRight,
                    targetPositionLeft,timeoutS,telemetry);
        } */
    }


}
