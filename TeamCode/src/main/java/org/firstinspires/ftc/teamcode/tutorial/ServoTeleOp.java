package org.firstinspires.ftc.teamcode.tutorial;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group = "Test")
public class ServoTeleOp extends LinearOpMode {

    //naming the servos
    Servo servo1;
    Servo servo2;

    public void initialize() {
        //initializing the servos to the hardware map
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
    }

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        while (opModeIsActive()) {

            //using gamepad 1 buttons for testing servo1 positions
            if (gamepad1.y) {
                //up
                servo1.setPosition(1);
                telemetry.addData("Servo1 position:", servo1.getPosition());
                telemetry.update();
            }
            if (gamepad1.x || gamepad1.b) {
                //half
                servo1.setPosition(0.5);
                telemetry.addData("Servo1 position:", servo1.getPosition());
                telemetry.update();
            }
            if (gamepad1.a) {
                //down
                servo1.setPosition(0);
                telemetry.addData("Servo1 position:", servo1.getPosition());
                telemetry.update();
            }

            //using gamepad 2 buttons for testing servo2 positions
            if (gamepad2.y) {
                //up
                servo2.setPosition(0);
                telemetry.addData("Servo2 position:", servo2.getPosition());
                telemetry.update();
            }
            if (gamepad2.x || gamepad2.b) {
                //half
                servo2.setPosition(0.5);
                telemetry.addData("Servo2 position:", servo2.getPosition());
                telemetry.update();
            }
            if (gamepad2.a) {
                //down
                servo2.setPosition(1);
                telemetry.addData("Servo2 position:", servo2.getPosition());
                telemetry.update();
            }

        }

    }

}

