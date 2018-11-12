package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

//Version 10.19.18 (fixed switching & made dif drive ways into methods)
@TeleOp(name = "TeleOp 12907", group = "teleop")
public class TeleOp12907 extends LinearOpMode {
    //Naming the motors
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor lift;
    Servo latch;
    BNO055IMU imu = null;

    final double LATCH_EXTENDED_POSITION = 0.8;
    final double LATCH_RETRACTED_POSITION = 0.2;


    /**
     * Initializes motors from the hardware map
     */
    public void initialize() {
        //Configuration of the motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        lift = hardwareMap.get(DcMotor.class, "lift");
        latch = hardwareMap.get(Servo.class, "latch");
        //Setting the direction of the motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);




    }

    //This method is for Tank Drive (a)
    public void tankDrive(double powerRight, double powerLeft) {
        //joystick goes from -1 to +1, so we will negate it
        powerLeft = -gamepad1.left_stick_y;
        powerRight = -gamepad1.right_stick_y;
        frontLeft.setPower(powerLeft*0.75);
        backLeft.setPower(powerLeft*0.75);
        frontRight.setPower(powerRight*0.75);
        backRight.setPower(powerRight*0.75);
    }

    //This method is for Arcade Drive (b)
    public void arcadeDrive(double powerRight, double powerLeft){
        //joystick goes from -1 to +1, so we will negate it
        double powerDrive = -gamepad1.left_stick_y;
        double powerTurn = gamepad1.right_stick_x;

        powerRight = Range.clip(powerDrive + powerTurn, -1.0, 1.0);
        powerLeft = Range.clip(powerDrive - powerTurn, -1.0, 1.0);
        frontLeft.setPower(powerLeft);
        backLeft.setPower(powerLeft);
        frontRight.setPower(powerRight);
        backRight.setPower(powerRight);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        double powerLeft = 0;
        double powerRight = 0;
        double powerLift = 0;
        double powerDrive;
        double powerTurn;
        boolean driveMode=false;
        boolean driveInput=false;
        while (opModeIsActive()) {
            //driveMode a is a tank drive, while driveMode b is an arcade style drive
            while (!driveInput) {
                if (gamepad1.a) {
                    driveMode = true;
                    driveInput=true;
                }
                if (gamepad1.b) {
                    driveMode = false;
                    driveInput=true;
                }
            }
            if (driveMode) {
                tankDrive(powerRight, powerLeft);
                telemetry.addData("Drive Mode: ", "Tank Drive");
                telemetry.update();

            }
            if (!driveMode) {
                arcadeDrive(powerRight, powerLeft);
                telemetry.addData("Drive Mode: ", "Arcade Drive");
                telemetry.update();
            }
            //servo latching

            if (gamepad2.x){
                latch.setPosition(LATCH_EXTENDED_POSITION);
            }
            if (gamepad2.a){
                latch.setPosition(LATCH_RETRACTED_POSITION);
            }
            //lift up and down
            powerLift = -gamepad2.right_stick_y;
            lift.setPower(powerLift);

            telemetry.addData("Power Left: ", powerLeft);
            telemetry.addData("Power Right: ", powerRight);
            telemetry.update();
            idle();
        }
    }

}
