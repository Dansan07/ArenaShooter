/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input;

/**
 *
 * @author Usuario
 */
public class AccelerometerInput {
    private final SerialManager serial;

    // Umbral de inclinación para considerar movimiento
    private static final int THRESHOLD = 200;

    public AccelerometerInput(SerialManager serial) {
        this.serial = serial;
    }

    public boolean isUp()    { return serial.accelY > THRESHOLD;  }
    public boolean isDown()  { return serial.accelY < -THRESHOLD; }
    public boolean isLeft()  { return serial.accelX < -THRESHOLD; }
    public boolean isRight() { return serial.accelX > THRESHOLD;  }
}
