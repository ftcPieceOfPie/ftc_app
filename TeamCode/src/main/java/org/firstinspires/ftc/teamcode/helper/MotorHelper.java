package org.firstinspires.ftc.teamcode.helper;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class MotorHelper {





    Orientation lastAngles;
    double globalAngle;

    private void resetAngle(BNO055IMU imu) {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    public void turnWithEncoders(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight,
                                 DcMotor backLeft, float angle, double power, BNO055IMU imu) {
         double currentAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                AngleUnit.DEGREES).firstAngle;
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        if (angle > 0) {
            while (((imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                    AngleUnit.DEGREES)).firstAngle)-currentAngle < angle) {
                frontRight.setPower(power);
                backRight.setPower(power);
                frontLeft.setPower(-power);
                backLeft.setPower(-power);
            }
        }
        else {
            while (((imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                    AngleUnit.DEGREES)).firstAngle)-currentAngle > angle) {
                frontRight.setPower(-power);
                backRight.setPower(-power);
                frontLeft.setPower(power);
                backLeft.setPower(power);

            }

        }
        frontRight.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);

    }





    public void liftActuator(DcMotor pLiftMotor, double pLiftPower, int pTargetPosition) {

        pLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pLiftMotor.setTargetPosition(pTargetPosition);
        pLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pLiftMotor.setPower(pLiftPower);
        pLiftMotor.setPower(0);
    }





    //Updated
    public void liftActuatorInches(DcMotor pLiftMotor, double pLiftPower, int pTargetPosition,
                                   double timeoutS, Telemetry telemetry) {

        ElapsedTime runtime = new ElapsedTime();

        int pNewTargetPosition;
        final double COUNTS_PER_MOTOR_DCMOTOR = 1120;    // eg: TETRIX Motor Encoder
        final double DRIVE_GEAR_REDUCTION = 0.5;     // This is < 1.0 if geared UP
        final double WHEEL_DIAMETER_INCHES = 1.0;     // For figuring circumference
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_DCMOTOR * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);

        pLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        pLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        pNewTargetPosition = pLiftMotor.getCurrentPosition() + (int) (pTargetPosition * COUNTS_PER_INCH);
        pLiftMotor.setTargetPosition(pNewTargetPosition);

        runtime.reset();
        pLiftMotor.setPower(Math.abs(pLiftPower));

        while (pLiftMotor.isBusy()){

            telemetry.addData("Lift Power: ", pLiftPower);
            telemetry.update();

           // Display it (position) for the driver.
            telemetry.addData("NewPosition", "Running to:%7d", pNewTargetPosition);
            telemetry.addData("CurrentPosition", "Running at:%7d", pLiftMotor.getCurrentPosition());
            telemetry.update();

            if(runtime.milliseconds()> timeoutS){
                break;
            }

        }
        pLiftMotor.setPower(0);
    }






    public void latch(Servo latch, double latchPosition, Telemetry telemetry) {

        latch.setPosition(latchPosition);
        telemetry.addData("Servo Position =", latchPosition);
        telemetry.update();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }





        /*
    1: Reset encoders
    2: Set target position
    3: Set power to motors (grouped to right and left)
    4: Run to target position
    5: Stop motors
     */
    public void movingWithEncoders(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft,
                                   double powerRight, double powerLeft, double targetPositionRight, double targetPositionLeft,
                                   double timeoutS, Telemetry telemetry) {

        ElapsedTime runtime = new ElapsedTime();

        final double COUNTS_PER_MOTOR_DCMOTOR = 1120;    // eg: TETRIX Motor Encoder
        final double DRIVE_GEAR_REDUCTION = 0.5;     // This is < 1.0 if geared UP
        final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_DCMOTOR * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);

        int newTargetPositionRight;
        int newTargetPositionLeft;

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Determine new target position, and pass to motor controller
        newTargetPositionLeft = backLeft.getCurrentPosition() + (int) (targetPositionLeft * COUNTS_PER_INCH);
        newTargetPositionRight = backRight.getCurrentPosition() + (int) (targetPositionRight * COUNTS_PER_INCH);
        backLeft.setTargetPosition(newTargetPositionLeft);
        backRight.setTargetPosition(newTargetPositionRight);

        runtime.reset();
        //commented out below is the power when it had absolute value
        /*frontRight.setPower(Math.abs(powerRight));
        backRight.setPower(Math.abs(powerRight));
        frontLeft.setPower(Math.abs(powerLeft));
        backLeft.setPower(Math.abs(powerLeft)); */
        backLeft.setPower(Math.abs(powerLeft));
        frontRight.setPower((powerRight));
        backRight.setPower((powerRight));
        frontLeft.setPower((powerLeft));
        backLeft.setPower((powerLeft));

        while ((runtime.seconds() < timeoutS) &&
                (backLeft.isBusy() && backRight.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newTargetPositionLeft, newTargetPositionRight);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    backLeft.getCurrentPosition(),
                    backRight.getCurrentPosition());
            telemetry.update();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //stop motors
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }






    public void forwardWithDistance(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft,
                                    double power, double distance, DistanceSensor dist){
        while(dist.getDistance(DistanceUnit.CM)>distance){
            frontRight.setPower(power);
            backRight.setPower(power);
            frontLeft.setPower(power);
            backLeft.setPower(power);
        }
        frontRight.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);

    }






    public void markerDrop(Servo markerDropper, double markerDropperPosition, Telemetry telemetry) {
        //Making sure that the servo position given doesn't exceed or go to low
        markerDropper.setPosition(Range.clip(markerDropperPosition, Servo.MIN_POSITION, Servo.MAX_POSITION));

        markerDropper.setPosition(markerDropperPosition);
        telemetry.addData("Servo Position =", markerDropperPosition);
        telemetry.update();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}




