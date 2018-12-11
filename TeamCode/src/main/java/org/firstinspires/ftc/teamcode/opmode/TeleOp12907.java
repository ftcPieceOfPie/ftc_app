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

//Version 10.19.18 (fixed switching & made dif drive ways into methods)
@TeleOp(name = "TeleOp 12907", group = "teleop")
public class TeleOp12907 extends LinearOpMode {
    //Naming the motors
    // DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor frontLeft;
    DcMotor lift;
    //DcMotor boom;
    DcMotor slide;
    DcMotor sweeper;
    Servo latch;
    //Servo markerDropper;
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
        //boom = hardwareMap.get(DcMotor.class, "boom");
        slide = hardwareMap.get(DcMotor.class, "slide");
        sweeper = hardwareMap.get(DcMotor.class, "sweeper");
        latch = hardwareMap.get(Servo.class, "latch");
        //markerDropper = hardwareMap.get(Servo.class, "markerDropper");
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
        //boom.setDirection(DcMotorSimple.Direction.FORWARD);
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

    //This method is for Tank Drive (a)
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

        //frontLeft.setPower(powerLeft*0.75);
        //backLeft.setPower(powerLeft*0.75);
        //frontRight.setPower(powerRight*0.75);
        //backRight.setPower(powerRight*0.75);

        //Quadratic Drive: the power is set to x squared
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

    /*//This method is for Arcade Drive (b)
    public void arcadeDrive(double powerRight, double powerLeft){
        //joystick goes from -1 to +1, so we will negate it
        double powerDrive = -gamepad1.left_stick_y;
        double powerTurn = gamepad1.right_stick_x;

        powerRight = Range.clip(powerDrive + powerTurn, -1.0, 1.0);
        powerLeft = Range.clip(powerDrive - powerTurn, -1.0, 1.0);
        frontLeft.setPower(powerLeft*0.75);
        backLeft.setPower(powerLeft*0.75);
        frontRight.setPower(powerRight*0.75);
        backRight.setPower(powerRight*0.75);
    }*/

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        /*boom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        boom.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

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
        while (opModeIsActive()) {


            //GAMEPAD 1:

            //driveMode a is a tank drive, while driveMode b is an arcade style drive
           /* while (!driveInput) {
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
            }*/

           tankDrive(powerRight, powerLeft);


            //GAMEPAD 2:

            //servo latching -->
            if (gamepad2.dpad_left){
                latch.setPosition(EXTENDED_POSITION);
            }
            if (gamepad2.dpad_right){
                latch.setPosition(RETRACTED_POSITION);
            }

            //lift up and down -->
            powerLift = -gamepad2.left_stick_y;
            lift.setPower(powerLift);

            //collection slider for reaching out to the crater
            //powerSlide = -gamepad2.right_stick_y;
            //slide.setPower(powerSlide * 0.5);

            //using triggers to put the slide out and in
            powerSlide = gamepad2.right_trigger;
            slide.setPower(powerSlide * 0.7);

            powerSlide = gamepad2.left_trigger;
            slide.setPower(-(powerSlide * 0.7));

            //using the right joystick to sweep in and out minerals
            double powerSweep = gamepad2.right_stick_y;
            sweeper.setPower(powerSweep);

            //right & left bumpers to adjust sweeping servo position


            /* if(gamepad2.right_bumper){
                double position = currentPosition + 0.05;
                sweeperDump.setPosition(position);
            }
            if(gamepad2.left_bumper){
                double position = currentPosition - 0.05;
                sweeperDump.setPosition(position);
            }*/


            //when right bumper pressed: increase position of sweeper servo by 0.05
            //when left bumper pressed: decrease position of sweeper servo by 0.05

            double currentPosition = sweeperDump.getPosition();

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
            }


            //using triggers to sweep (forwards & reverse)
           /*double powerSweep = gamepad2.right_trigger;
            sweeper.setPower(powerSweep);
            telemetry.addData("Sweeping Power: ", powerSweep);
            telemetry.update();

            powerSweep = gamepad2.left_trigger;
            sweeper.setPower(-powerSweep);
            telemetry.addData("Sweeping Power: ", powerSweep);
            telemetry.update(); */

            //putting minerals from sweeper into dumper
            if (gamepad2.y){
                //sweeper.setPower(0);

                //put minerals into dumper position
                sweeperDump.setPosition(0.8);
                telemetry.addData("Sweeper dump position: ", sweeperDump.getPosition());
                telemetry.update();
            }

            if (gamepad2.x) {
                //sweeper.setPower(0);

                //halfway sweeper servo position
                sweeperDump.setPosition(0.45);
                telemetry.addData("Sweeper dump position: ", sweeperDump.getPosition());
                telemetry.update();
            }

            if (gamepad2.a){

                //Putting sweeper down
                sweeperDump.setPosition(0.27);
                telemetry.addData("Sweeper dump position: ", sweeperDump.getPosition());
                telemetry.update();

                /*while (true) {
                    sweeper.setPower(1);

                    if (gamepad2.x){
                        break;
                    }
                }*/
            }



            //sweeping in minerals using right bumper (toggled on and off)
           /* boolean powerEnableIn = false;
            boolean rightBumperPressed = false;

            if(gamepad2.right_bumper){
                if (!rightBumperPressed) {
                    powerEnableIn = !powerEnableIn;
                    rightBumperPressed = true;
                } else { }
             } else {
                rightBumperPressed = false;
            }

            if(powerEnableIn) {
                sweeper.setPower(1.0);
            } else {
                //sweeper.setPower(0.0);
            }


            //sweeping out minerals using left bumper (toggled on and off)
            boolean powerEnableOut = false;
            boolean leftBumperPressed = false;

            if(gamepad2.left_bumper){
                if(!leftBumperPressed) {
                    powerEnableOut = !powerEnableOut;
                    leftBumperPressed = true;
                } else { }
            } else {
                leftBumperPressed = false;
            }

            if(powerEnableOut) {
                sweeper.setPower(-1.0);
            } else {
                //sweeper.setPower(0.0);
            }*/



            //GAMEPAD 1:
            //dumping minerals into lander - 3 stages (down, lifted halfway, up&dump)
            if (gamepad1.y){
                mineralDump.setPosition(EXTENDED_POSITION);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();
            }

            if(gamepad1.x) {
                mineralDump.setPosition(HALF_EXTENDED_POSITION);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();
            }

            if (gamepad1.a){
                mineralDump.setPosition(RETRACTED_POSITION);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();
            }

            if (gamepad1.b) {
                mineralDump.setPosition(0.3);
                telemetry.addData("Mineral dump position: ", mineralDump.getPosition());
                telemetry.update();

            }









            //boom.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
            // boom.setPower(0);

            //Boom motor extend and retract -->
            /*if (gamepad2.a){

                    telemetry.addData("Which pressed: ", "a");
                telemetry.addData("Boom Current Position before moving: ", boom.getCurrentPosition());
                    telemetry.update();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                boom.setTargetPosition(boom.getCurrentPosition() + 100);
                boom.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                boom.setPower(0.5);

               while(boom.isBusy()){

                }

                telemetry.addData("Boom Current Position: ", boom.getCurrentPosition());
                telemetry.update();
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            }




            if (gamepad2.b){

                telemetry.addData("Which pressed: ", "b");
                telemetry.addData("Boom Current Position before moving: ", boom.getCurrentPosition());
                telemetry.update();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                boom.setTargetPosition(boom.getCurrentPosition() - 100);
                boom.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                boom.setPower(0.5);

                while(boom.isBusy()){

                }

                telemetry.addData("Boom Current Position: ", boom.getCurrentPosition());
                telemetry.update();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                telemetry.addData("Which pressed: ", "b");
                telemetry.update();

               try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                boom.setTargetPosition(500);
                boom.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                boom.setPower(-0.5);

                while(boom.isBusy()){
                    telemetry.addData("Boom Current Position: ", boom.getCurrentPosition());
                    telemetry.update();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            } */




            //dropping the marker -->
            /*if(gamepad2.y) {
                markerDropper.setPosition(LATCH_EXTENDED_POSITION);
            }
            if(gamepad2.b){
                markerDropper.setPosition(LATCH_RETRACTED_POSITION);
            } */

            telemetry.addData("Power Left: ", powerLeft);
            telemetry.addData("Power Right: ", powerRight);
            telemetry.update();

            idle();



        }
    }

}
