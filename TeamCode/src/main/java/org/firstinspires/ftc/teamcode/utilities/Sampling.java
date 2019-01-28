package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.helper.MotorHelper;
import org.firstinspires.ftc.teamcode.helper.SensorHelper;

public class Sampling {
    //Commented on 01/21 - from 1000 to 500
    final long SLEEP_TIME_500 = 500;
    final long SLEEP_TIME_250 = 250;
    final double EXTEND_OUT = 0.55;
    final double EXTEND_IN = 0.2;

    public void forwardWithColorSensor(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, MotorHelper motorHelper, SensorHelper sensorHelper,
                                       Telemetry telemetry, ColorSensor middleColor, DistanceSensor distanceSensor, Servo rightArm, Servo leftArm, Servo rightKnocker,
                                       Servo leftKnocker, ColorSensor knockerColor) {

        //moving forward to minerals to begin detection
        //WITHOUT DISTANCE SENSOR - (fixed distance)
        /*double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 17.5;
        double targetPositionRight = 17.5;
        double timeoutS = 5;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                targetPositionLeft, timeoutS, telemetry);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/





        //USING DISTANCE SENSOR - going forwards to minerals:
        //Going forward 15" then measuring distance
        //If measured distance is greater than 3"(tgtDistToMineral)
        //then set a new target position of the 'measured distance' minus 'tgtDistToMineral'
        //pass this difference into the moving with encoders method

        //double tgtDistToMineral = 2;
        double tgtDistToMineral = 2.15;


        double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 15;
        double targetPositionRight = 15;
        double timeoutS = 5;
        motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                targetPositionLeft, timeoutS, telemetry);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double distanceMeasured = (distanceSensor.getDistance(DistanceUnit.INCH));

        telemetry.addData("Distance from mineral: ", distanceMeasured);
        telemetry.update();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(distanceMeasured > tgtDistToMineral) {
            double newTargetPosition = (distanceMeasured - tgtDistToMineral);
            telemetry.addData("Distance to reach mineral: ", newTargetPosition);
            telemetry.update();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            powerRight = 0.25;
            powerLeft = 0.25;
            targetPositionLeft = newTargetPosition;
            targetPositionRight = newTargetPosition;
            timeoutS = 5;
            motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft, powerRight, powerLeft, targetPositionRight,
                    targetPositionLeft, timeoutS, telemetry);

        } else {
            telemetry.addData("Distance from mineral: ", "sufficient!");
            telemetry.update();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //do nothing and move on to knocking the minerals
        }





        // Detect whether center block is yellow
        boolean whiteMiddle = sensorHelper.isWhite(middleColor, telemetry);
        telemetry.addData("middleColor: ", whiteMiddle);
        telemetry.update();
        //changed time from 500 to 250
        /*try {
            Thread.sleep(SLEEP_TIME_250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        if (!whiteMiddle) {
            knockMiddleYellow(motorHelper, frontRight, frontLeft, backRight, backLeft, telemetry);

        } else {
            //color sensor arm dropped down to determine if the left is yellow

            leftKnocker.setPosition(0.825);

            try {
                Thread.sleep(SLEEP_TIME_500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //leftArm.setPosition(0.575);
            leftArm.setPosition(0.6);

            try {
                Thread.sleep(SLEEP_TIME_500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean whiteLeft = sensorHelper.isWhite(knockerColor, telemetry);
            telemetry.addData("leftColor: ", whiteLeft);
            telemetry.update();

            if (!whiteLeft) {
                //if left is yellow cube, left arm goes knocks off cube
                knockLeftYellow(leftKnocker, leftArm);

            } else {
                //else, the right side would have the cube, and it would knock it off with the right arm/knocker
                knockRightYellow(leftKnocker, leftArm, rightArm, rightKnocker);
            }

            //in case it doesn't go back up, setting both arms back up

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
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //if distance is more than 6 cm from the mineral, go forwards until reached 6 cm - NEEDS WORK!!
        //motorHelper.forwardWithDistance(frontRight,frontLeft,backRight,backLeft, 0.25, 6, distance, telemetry);

        if (yellowPosition.equalsIgnoreCase("Center")) {
            knockMiddleYellow(motorHelper, frontRight, frontLeft, backRight, backLeft, telemetry);

        } else {
            //Putting arm down (originally for color sensing)
            /*leftKnocker.setPosition(0.825);

            try {
                Thread.sleep(SLEEP_TIME_1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            leftArm.setPosition(0.575);

            try {
                Thread.sleep(SLEEP_TIME_1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            if (yellowPosition.equalsIgnoreCase("Left")) {
                //if left is yellow cube, left arm goes knocks off cube
                knockLeftYellow(leftKnocker, leftArm);

            } else {
                //else, the right side would have the cube, and it would knock it off with the right arm/knocker
                knockRightYellow(leftKnocker, leftArm, rightArm, rightKnocker);

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
        leftKnocker.setPosition(0.7);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //leftArm.setPosition(0.8);
        leftArm.setPosition(0.75);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftKnocker.setPosition(1);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftArm.setPosition(0.2);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftKnocker.setPosition(0);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void knockRightYellow(Servo leftKnocker, Servo leftArm, Servo rightArm, Servo rightKnocker) {

        leftArm.setPosition(0.2);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftKnocker.setPosition(0);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        rightKnocker.setPosition(0.6);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //rightArm.setPosition(0);
        rightArm.setPosition(0);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        rightKnocker.setPosition(0.2);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //arm coming back in positions:

        rightArm.setPosition(0.8);
        //rightArm.setPosition(0.6);
        //rightArm.setPosition(0.75);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rightKnocker.setPosition(1);

        try {
            Thread.sleep(SLEEP_TIME_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
