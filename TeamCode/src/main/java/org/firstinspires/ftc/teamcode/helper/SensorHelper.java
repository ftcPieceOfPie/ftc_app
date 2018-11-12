package org.firstinspires.ftc.teamcode.helper;

        import com.qualcomm.robotcore.hardware.ColorSensor;
        import com.qualcomm.robotcore.hardware.Servo;

        import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SensorHelper {
    public boolean isYellow (ColorSensor color){
        float hsvValues[] = {0F, 0F, 0F};

        final float values[] = hsvValues;

        final double SCALE_FACTOR = 255;

        int red = (int) (color.red() * SCALE_FACTOR);
        int green = (int) (color.green()*SCALE_FACTOR);
        if (red<300&&green<300){
            return false;
        }
        else{
            return true;
        }


    }
    public boolean isWhite(ColorSensor color, Telemetry telemetry) {
        float hsvValues[] = {0F, 0F, 0F};

        final float values[] = hsvValues;

        final double SCALE_FACTOR = 255;

        int red = (int) (color.red() * SCALE_FACTOR);
        int green = (int) (color.green()*SCALE_FACTOR);
        telemetry.addData("Red: ", red);
        telemetry.addData("Green: ", green);
        telemetry.update();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (red<300&&green<300){
            return true;
        }
        else {
            return false;


        }
    }

}

