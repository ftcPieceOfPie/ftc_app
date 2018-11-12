/*package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.helper.MotorHelper;

@Autonomous(name = "AutonomousTest_Encoders", group = "autonomous")
public class Autonomous12907 extends LinearOpMode {
    //Naming the motors
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    MotorHelper motorHelper;

    public void initialize() {
        //Configuration of the motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        //setting the directions of the motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        motorHelper = new MotorHelper();
        double powerRight = 0.25;
        double powerLeft = 0.25;
        double targetPositionLeft = 12;
        double targetPositionRight = 12;
        double timeoutS = 5;
        if (opModeIsActive()) {
            motorHelper.movingWithEncoders(frontRight, frontLeft, backRight, backLeft,
                    powerRight, powerLeft,
                    targetPositionRight, targetPositionLeft,
                    timeoutS, telemetry);
        }
    }
}
*/

package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.helper.MotorHelper;
import org.firstinspires.ftc.teamcode.helper.SensorHelper;
import org.firstinspires.ftc.teamcode.utilities.Landing;
import org.firstinspires.ftc.teamcode.utilities.Marker;
import org.firstinspires.ftc.teamcode.utilities.Parking;
import org.firstinspires.ftc.teamcode.utilities.Sampling;

//Adding Source Code to GitHub

@Autonomous(name = "Autonomous 2019", group = "autonomous")
public class Autonomous12907 extends LinearOpMode {
    //Naming the motors
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    Servo latch;
    Servo markerDropper;
    DcMotor liftActuator;
    BNO055IMU imu;
    DistanceSensor dist;
    Orientation lastAngles;
    ColorSensor markerColor;
    ColorSensor middleColor;

    //Initializes motors from the hardware map

    public void initialize() {
        //Configuration of the motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        liftActuator = hardwareMap.get(DcMotor.class, "lift");
        latch = hardwareMap.get(Servo.class, "latch");
        dist= hardwareMap.get(DistanceSensor.class, "sensorDistance");
        markerDropper = hardwareMap.get(Servo.class, "markerDropper");
        //Setting the direction of the motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        liftActuator.setDirection(DcMotorSimple.Direction.FORWARD);
        markerColor = hardwareMap.get(ColorSensor.class, "markerColor");
        middleColor = hardwareMap.get(ColorSensor.class, "middleColor");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.mode = BNO055IMU.SensorMode.IMU;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


    }


    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        Landing landing = new Landing();
        Sampling sampling = new Sampling();
        Marker marker = new Marker();
        Parking parking = new Parking();
        MotorHelper motorHelper = new MotorHelper();
        SensorHelper sensorHelper = new SensorHelper();
        waitForStart();

        if (opModeIsActive()) {
            //landing then unlatching - WORKING
            landing.drop(liftActuator, latch, motorHelper, telemetry);
            //sleep(500);
            //moving forward for Sampling - WORKING
            sampling.forward(frontRight, frontLeft, backRight, backLeft, motorHelper, sensorHelper, telemetry, middleColor);
            //sleep(5000);
            //moving to depot - WORKING
            marker.dropMarkerToDepot(frontRight, frontLeft, backRight, backLeft, motorHelper, telemetry, imu, markerDropper, markerColor);

        }
    }
}
