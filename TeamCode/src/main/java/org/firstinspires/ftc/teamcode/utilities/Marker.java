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

public class Marker {

    public void dropMarkerToDepot(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, MotorHelper motorHelper, Telemetry telemetry, BNO055IMU imu, ColorSensor color, DistanceSensor distance, Servo sweeperDump, DcMotor sweeper) {

        //going back 8 inches
        double powerRight = -0.5;
        double powerLeft = -0.5;
        double targetPositionLeft = -5;
        double targetPositionRight = -5;
        double timeoutS = 3;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //turning towards wall - CORRECT THIS!!!
        float angle = 55;
        double turnPower = 0.5;
        motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //going forwards 30 inches to wall
        powerRight = 0.5;
        powerLeft = 0.5;
        targetPositionLeft = 30;
        targetPositionRight = 30;
        timeoutS = 6;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //detect if pie is white or black with color sensor
        //turnRight is a boolean, it will read either true or false
        SensorHelper sensorHelper = new SensorHelper();
        boolean turnRight = sensorHelper.isWhite(color, distance, telemetry);
        telemetry.addData("turnRight: ",turnRight);
        telemetry.update();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //if it's white (true), turn right
        if(turnRight){
            angle = -60;
            turnPower = 0.5;
            motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu);
            telemetry.addData("turn: ","Right");
            telemetry.update();
        }
        //or if it's not (it's black - false), turn left
        else{
            angle = 20;
            turnPower = 0.5;
            motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu);
            telemetry.addData("turn: ","Left");
            telemetry.update();
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        Turn done in previous code where turn depends on marker color
        //turning towards depot
        angle = -60;
        turnPower = 0.5;
        motorHelper.turnWithEncoders(frontRight, frontLeft, backRight, backLeft, angle, turnPower, imu);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        //go forwards 4 feet to the depot
        //subtract 5 inches for dropping marker with sweeper servo extended length
        targetPositionLeft = (45 - 10);
        targetPositionRight = (45 - 10);
        timeoutS = 6;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);


        //drop marker with servo movements
       double markerDropPositionDown = 0.28;
       double markerDropPositionUp = 0.8;

        //timeoutS = 5000;
        motorHelper.markerDrop(sweeperDump, markerDropPositionDown, markerDropPositionUp, sweeper, telemetry);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        //go backwards 48 feet (INTO CRATER)
        //PARKING!!
        //if it's white (true), wall follower right
        if(turnRight){
            powerRight = -0.6;
            powerLeft = -0.5;
            targetPositionLeft = (-70 + 5);
            targetPositionRight = (-70 + 5);
            timeoutS = 10;
            motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);

        }
        //or if it's not (it's black - false), wall follower left
        else{
            powerRight = -0.5;
            powerLeft = -0.6;
            targetPositionLeft = (-70 + 10);
            targetPositionRight = (-70 + 10);
            timeoutS = 10;
            motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);

        }

        /*
        //Going back into crater is done ABOVE depending on what turn
        powerRight = -0.6;
        powerLeft = -0.5;
        targetPositionLeft = -70;
        targetPositionRight = -70;
        timeoutS = 10;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight, targetPositionLeft, timeoutS, telemetry);
        */

    }

}
