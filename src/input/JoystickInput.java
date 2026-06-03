/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input;

/**
 *
 * @author Usuario
 */
public class JoystickInput {
        private final SerialManager serial;

    // Zona muerta para evitar drift
    private static final int DEAD_ZONE = 100;
    private static final int CENTER = 512;

    public JoystickInput(SerialManager serial) {
        this.serial = serial;
    }

    public boolean isUp()    { return serial.joyY < CENTER - DEAD_ZONE; }
    public boolean isDown()  { return serial.joyY > CENTER + DEAD_ZONE; }
    public boolean isLeft()  { return serial.joyX < CENTER - DEAD_ZONE; }
    public boolean isRight() { return serial.joyX > CENTER + DEAD_ZONE; }
    public boolean isShoot() { return serial.joyButton; }

}
