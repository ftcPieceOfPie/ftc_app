package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helper.MotorHelper;
import org.firstinspires.ftc.teamcode.helper.SensorHelper;

public class Sampling {

    final double EXTEND_OUT = 0.55;
    final double EXTEND_IN = 0.2;

    public void forwardWithColorSensor(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, MotorHelper motorHelper, SensorHelper sensorHelper,
                                       Telemetry telemetry, ColorSensor middleColor, DistanceSensor distance, Servo rightArm, Servo leftArm, Servo rightKnocker,
                                       Servo leftKnocker, ColorSensor knockerColor) {


        double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 17.5;
        double targetPositionRight = 17.5;
        double timeoutS = 5;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                targetPositionLeft, timeoutS, telemetry);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Detect whether center block is yellow
        boolean whiteMiddle = sensorHelper.isWhite(middleColor, distance, telemetry);
        telemetry.addData("middleColor: ", whiteMiddle);
        telemetry.update();
        if (!whiteMiddle) {
            knockMiddleYellow(motorHelper, frontRight, frontLeft, backRight, backLeft, telemetry);

        } else {
            //color sensor arm dropped down to determine if the left is yellow
            leftKnocker.setPosition(0.275);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            leftArm.setPosition(EXTEND_OUT);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean whiteLeft = sensorHelper.isWhite(knockerColor, distance, telemetry);
            telemetry.addData("leftColor: ", whiteLeft);
            telemetry.update();
            if (!whiteLeft) {
                //if left is yellow cube, left arm goes knocks off cube
                knockLeftYellow(leftKnocker, leftArm);

            } else {
                //else, the right side would have the cube, and it would knock it off with the right arm/knocker
                knockRightYellow(leftArm, rightArm, rightKnocker);

            }

            //in case it doesn't go back up

            leftArm.setPosition(0);

            rightArm.setPosition(1);
        }

    }

    public void forwardWithTensor(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, MotorHelper motorHelper, SensorHelper sensorHelper,
                                  Telemetry telemetry, DistanceSensor distance, Servo rightArm, Servo leftArm, Servo rightKnocker,
                                  Servo leftKnocker, String yellowPosition) {
        //moving forward to minerals
        double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 17.5;
        double targetPositionRight = 17.5;
        double timeoutS = 5;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                targetPositionLeft, timeoutS, telemetry);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (yellowPosition.equalsIgnoreCase("Center")) {
            knockMiddleYellow(motorHelper, frontRight, frontLeft, backRight, backLeft, telemetry);

        } else {
            //
            leftKnocker.setPosition(0.275);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            leftArm.setPosition(EXTEND_OUT);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (yellowPosition.equalsIgnoreCase("Left")) {
                //if left is yellow cube, left arm goes knocks off cube
                knockLeftYellow(leftKnocker, leftArm);

            } else {
                //else, the right side would have the cube, and it would knock it off with the right arm/knocker
                knockRightYellow(leftArm, rightArm, rightKnocker);

            }

            //in case it doesn't go back up

            leftArm.setPosition(0);

            rightArm.setPosition(1);
        }


    }

    /*
    3 methods below are for Servo & Motor positions for knocking the gold cube when it's in the middle, left, or right:
     */

    private void knockMiddleYellow(MotorHelper motorHelper, DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, Telemetry telemetry) {
        double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 4;
        double targetPositionRight = 4;
        double timeoutS = 5;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                targetPositionLeft, timeoutS, telemetry);
        telemetry.addData("Travel Forward: ", "yes");
        telemetry.update();
        powerRight = -0.25;
        powerLeft = -0.25;
        targetPositionLeft = -4;
        targetPositionRight = -4;
        timeoutS = 5;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                targetPositionLeft, timeoutS, telemetry);
    }

    private void knockLeftYellow(Servo leftKnocker, Servo leftArm) {
        leftKnocker.setPosition(0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftArm.setPosition(0.8);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftKnocker.setPosition(0.4);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftArm.setPosition(EXTEND_IN);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void knockRightYellow(Servo leftArm, Servo rightArm, Servo rightKnocker) {

        leftArm.setPosition(EXTEND_IN);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rightKnocker.setPosition(0.6);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rightArm.setPosition(0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rightKnocker.setPosition(0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rightArm.setPosition(0.6);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}