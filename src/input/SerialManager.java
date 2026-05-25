/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class SerialManager {
    
    SerialPort port;
    
    public int xValue;
    public int yValue;
    public int buttonValue;
    
    public void connect() {

        SerialPort[] ports = SerialPort.getCommPorts();

        port = ports[0];

        port.openPort();

        port.setBaudRate(9600);

        Thread thread = new Thread(() -> {

            Scanner scanner = new Scanner(port.getInputStream());

            while (scanner.hasNextLine()) {
                try {
                    String line = scanner.nextLine();
                    String[] data = line.split(",");

                    xValue = Integer.parseInt(data[0]);
                    yValue = Integer.parseInt(data[1]);
                    buttonValue = Integer.parseInt(data[2]);

                    System.out.println(
                            xValue + " "
                            + yValue + " "
                            + buttonValue
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    
}
