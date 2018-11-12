package org.firstinspires.ftc.teamcode.tutorial;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp  (name ="Servo Test", group = "Test")
public class ServoTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorTest = null;
        Servo servoTest = null;
        double tgtPower = 0;
        waitForStart();
         while (opModeIsActive()) {
            tgtPower = -this.gamepad1.left_stick_y;
            motorTest.setPower(tgtPower);
            if (gamepad1.y) {
                servoTest.setPosition(0);
            }else if(gamepad1.x|| gamepad1.b) {
                servoTest.setPosition(0.05);
            }else if(gamepad1.a){
                servoTest.setPosition(1);
            }

        }
    }
}
