package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helper.MotorHelper;
import org.firstinspires.ftc.teamcode.helper.SensorHelper;

//dropping our team marker
public class Marker {

    final long SLEEP_TIME_250 = 250;

    public void dropMarkerToDepot(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, MotorHelper motorHelper, Telemetry telemetry, BNO055IMU imu, Servo sweeperDump, DcMotor sweeper, boolean turnRight) {

        //going back 8 inches
        double powerRight = -0.5;
        double powerLeft = -0.5;
        double targetPositionLeft = -5;
        double targetPositionRight = -5;
        double timeoutS = 3;
        //motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);
        try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //turning towards wall - working
        float angle = 60;
        double turnPower = 0.5;
        //motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu, telemetry);
        try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //going forwards 25 inches to wall
        //Commented on 01/21 --> changing target positions 25 to 30
        //Commented on 01/22 --> changing target positions 30 50 32
        powerRight = 0.5;
        powerLeft = 0.5;
        targetPositionLeft = 30;
        targetPositionRight = 30;
        timeoutS = 6;
        //motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);
        try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // the color sensing of the chip is now done in initialization of autonomous

        //detect if the chip is white or black with color sensor
        //turnRight is a boolean, it will read either true or false
        /*SensorHelper sensorHelper = new SensorHelper();
        boolean turnRight = sensorHelper.isWhite(colorSensor, distance, telemetry);
        telemetry.addData("turnRight: ",turnRight);
        telemetry.update();
        try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //if it's white (true), turn right
        if(turnRight){
            angle = -85;
            turnPower = 0.5;
            //motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu, telemetry);
            telemetry.addData("turn: ","Right");
            telemetry.update();
        }
        //or if it's not (it's black - false), turn left
        else{
            angle = 20;
            turnPower = 0.5;
            //motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu, telemetry);
            telemetry.addData("turn: ","Left");
            telemetry.update();
        }
        try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //go forwards 4 feet to the DEPOT
        //subtract 5 inches for dropping marker with sweeper servo extended length
        //targetPositionLeft = (35);
        //targetPositionRight = (35);
        targetPositionLeft = (29);
        targetPositionRight = (29);
        timeoutS = 6;
        //motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);


        //drop marker with servo movements
       double markerDropPositionDown = 0.4;
       double markerDropPositionUp = 0.9;

        //timeoutS = 5000;
        motorHelper.markerDrop(sweeperDump, markerDropPositionDown, markerDropPositionUp, sweeper, telemetry);

        /*try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/



        //go backwards 48 feet (INTO CRATER)
        //PARKING!!
        //if it's white (true), wall follower right

        //double craterTargetPosition = -56;
        double craterTargetPosition = -58;
        if(turnRight){
            powerRight = -0.6;
            powerLeft = -0.5;
            targetPositionLeft = craterTargetPosition;
            targetPositionRight = craterTargetPosition;
            timeoutS = 10;
            //motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);

        }
        //or if it's not (it's black - false), wall follower left
        else{
            powerRight = -0.5;
            powerLeft = -0.6;
            targetPositionLeft = craterTargetPosition;
            targetPositionRight = craterTargetPosition;
            timeoutS = 10;
            //motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);

        }
    }

}
