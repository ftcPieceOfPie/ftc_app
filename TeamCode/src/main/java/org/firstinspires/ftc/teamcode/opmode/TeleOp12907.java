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
import org.firstinspires.ftc.teamcode.helper.MotorHelper;

//TeleOp code for gamepads
//Version 10.19.18 (fixed switching & made dif drive ways into methods)
@TeleOp(name = "TeleOp 12907", group = "teleop")
public class TeleOp12907 extends LinearOpMode {
    //Naming the motors
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor frontLeft;
    DcMotor lift;
    DcMotor slide;
    DcMotor sweeper;
    Servo latch;
    Servo sweeperDump;
    Servo mineralDump;
    Servo rightKnocker;
    Servo leftKnocker;
    BNO055IMU imu = null;

    final double EXTENDED_POSITION = 0.8;
    final double HALF_EXTENDED_POSITION = 0.7;
    final double RETRACTED_POSITION = 0.2;


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
        slide = hardwareMap.get(DcMotor.class, "slide");
        sweeper = hardwareMap.get(DcMotor.class, "sweeper");
        latch = hardwareMap.get(Servo.class, "latch");
        sweeperDump = hardwareMap.get(Servo.class, "sweeperDump");
        mineralDump = hardwareMap.get(Servo.class, "mineralDump");
        rightKnocker = hardwareMap.get(Servo.class, "rightKnocker");
        leftKnocker = hardwareMap.get(Servo.class, "leftKnocker");


        //Setting the direction of the motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setDirection(DcMotorSimple.Direction.FORWARD);
        sweeper.setDirection(DcMotorSimple.Direction.FORWARD);

        rightKnocker.setPosition(1);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftKnocker.setPosition(0);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //This method is for Tank Drive:
    public void tankDrive(double powerRight, double powerLeft) {
        //joystick goes from -1 to +1, so we will negate it
        powerLeft = -gamepad1.left_stick_y;
        powerRight = -gamepad1.right_stick_y;

        //Since using quadratic drive, scale factor implements not full power - for more control

        double powerScaleFactor = 0.9;

       //changing quadratic drive speed for alligning to the lander
        if (gamepad1.left_bumper){
            powerScaleFactor = 0.6;
        }

        if (gamepad1.right_bumper) {
            powerScaleFactor= 0.9;
        }

        //Quadratic Drive: the power is set to x squared
        //Math: power cubed over the absolute power = power squared --> to keep the sign of the orig pwr
        if (powerLeft != 0) {
            frontLeft.setPower(Math.pow(powerLeft * powerScaleFactor , 3)/Math.abs(powerLeft * powerScaleFactor));
            backLeft.setPower(Math.pow(powerLeft * powerScaleFactor, 3)/Math.abs(powerLeft * powerScaleFactor));

        } else {
            frontLeft.setPower(powerLeft);
            backLeft.setPower(powerLeft);
        }

        if (powerRight != 0) {
            frontRight.setPower(Math.pow(powerRight * powerScaleFactor, 3)/Math.abs(powerRight * powerScaleFactor));
            backRight.setPower(Math.pow(powerRight * powerScaleFactor, 3)/Math.abs(powerRight * powerScaleFactor));
        } else {
            frontRight.setPower(powerRight);
            backRight.setPower(powerRight);
        }

    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        waitForStart();

        double powerLeft = 0;
        double powerRight = 0;
        double powerLift = 1;
        double powerSlide = 1;
        //double powerSweep = 1;
        //double powerDrive;
        //double powerTurn;
        boolean driveMode=false;
        boolean driveInput=false;
        MotorHelper motorHelper = new MotorHelper();

        boolean threadRunning = false;



        class SweeperThread extends Thread{
            public void run (){
                while(true){
                    sweeper.setPower(1);
                    if(gamepad2.right_bumper || gamepad2.left_bumper){
                        sweeper.setPower(0);
                        break;
                    }
                }
            }

        }


        Thread ourSweeperThread = new SweeperThread();

        while (opModeIsActive()) {


            //GAMEPAD 1:

            //calling the method that gives the drivetrain power using joysticks
            tankDrive(powerRight, powerLeft);

            //dumping minerals into lander - 3 stages (down, lifted halfway, up&dump)
            if (gamepad1.y) {
                mineralDump.setPosition(EXTENDED_POSITION);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();
            }

            if (gamepad1.x) {
                mineralDump.setPosition(HALF_EXTENDED_POSITION);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();
            }

            if (gamepad1.a) {
                mineralDump.setPosition(RETRACTED_POSITION);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();
            }

            if (gamepad1.b) {
                mineralDump.setPosition(0.3);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();

            }


            //GAMEPAD 2:

            //servo (hook) latching -->
            if (gamepad2.dpad_left) {
                latch.setPosition(EXTENDED_POSITION);
            }
            if (gamepad2.dpad_right) {
                latch.setPosition(RETRACTED_POSITION);
            }

            //lift linear actuator up and down for latching -->
            powerLift = -gamepad2.left_stick_y;
            lift.setPower(powerLift);

            //using triggers to put the slide out and in
            powerSlide = gamepad2.left_trigger;
            slide.setPower(powerSlide * 0.9);

            powerSlide = gamepad2.right_trigger;
            slide.setPower(-(powerSlide * 0.9));

            //using the right joystick to sweep in and out minerals
            double powerSweep = gamepad2.right_stick_y;
            sweeper.setPower(powerSweep);


            //putting minerals from sweeper into dumper
            if (gamepad2.a) {
                //Putting sweeper down
                //sweeperDump.setPosition(0.3);
                sweeperDump.setPosition(0.4);

            }

            if (gamepad2.x) {
                //halfway sweeper servo position
                //sweeperDump.setPosition(0.45);
                sweeperDump.setPosition(0.6);

            }

            if (gamepad2.y) {
                //put minerals into mineral dumper
                //sweeperDump.setPosition(0.8);
                sweeperDump.setPosition(1);
            }

            if(gamepad2.right_bumper){
                if(!threadRunning){
                    ourSweeperThread.start();
                    threadRunning=true;
                }
                else{
                    threadRunning=false;
                }
            }

            if(gamepad2.left_bumper){
                threadRunning=false;
            }





            //when right bumper pressed: increase position of sweeper servo by 0.05
            //when left bumper pressed: decrease position of sweeper servo by 0.05

            /*double currentPosition = sweeperDump.getPosition();

            boolean rightBumperPressed = false;
            boolean leftBumperPressed = false;

            if (gamepad2.right_bumper) {
                if(!rightBumperPressed) {
                    currentPosition = sweeperDump.getPosition();
                    rightBumperPressed = true;

                    if(currentPosition < 0.9) {
                        sweeperDump.setPosition(currentPosition + 0.05);
                    }
                    //sweeperDump.setPosition(powerSweeperDumper);
                } else {}
            } else {
                rightBumperPressed = false;
            }

            if (gamepad2.left_bumper) {
                if(!leftBumperPressed)
                {
                    currentPosition = sweeperDump.getPosition();
                    leftBumperPressed = true;

                    if(currentPosition > 0.1) {
                        sweeperDump.setPosition(currentPosition - 0.05);
                    }
                    //sweeperDump.setPower(powerSweeperDumper);
                }
                else {}
            } else {
                leftBumperPressed = false;
            }*/

            telemetry.addData("Power Left: ", powerLeft);
            telemetry.addData("Power Right: ", powerRight);
            telemetry.update();

            idle();

        }
    }

}