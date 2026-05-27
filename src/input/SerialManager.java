/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
/**
 *
 * @author Usuario
 */
public class SerialManager {
   private SerialPort serialPort;
    private String buffer = "";

    // Joystick (jugador 1) — valores 0..1023
    public int joyX = 512;
    public int joyY = 512;
    public boolean joyButton = false;

    // Acelerómetro (jugador 2) — valores en mg o raw según tu sensor
    public int accelX = 0;
    public int accelY = 0;

    public void connect() {
        String[] ports = SerialPortList.getPortNames();
        if (ports.length == 0) {
            System.out.println("No se encontró puerto serial.");
            return;
        }

        // Toma el primer puerto disponible; cámbialo si necesitas uno específico
        String portName = ports[0];
        serialPort = new SerialPort(portName);

        try {
            serialPort.openPort();
            serialPort.setParams(
                SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
            );
            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.isRXCHAR()) {
                        try {
                            buffer += serialPort.readString();
                            procesarBuffer();
                        } catch (SerialPortException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            System.out.println("Conectado a: " + portName);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formato esperado desde Arduino:
     * "JX:512,JY:300,JB:0,AX:150,AY:-80\n"
     */
    private void procesarBuffer() {
        int idx;
        while ((idx = buffer.indexOf('\n')) != -1) {
            String linea = buffer.substring(0, idx).trim();
            buffer = buffer.substring(idx + 1);
            parsearLinea(linea);
        }
    }

    private void parsearLinea(String linea) {
        try {
            String[] partes = linea.split(",");
            for (String parte : partes) {
                String[] kv = parte.split(":");
                if (kv.length != 2) continue;
                String clave = kv[0].trim();
                int valor = Integer.parseInt(kv[1].trim());
                switch (clave) {
                    case "JX" -> joyX = valor;
                    case "JY" -> joyY = valor;
                    case "JB" -> joyButton = (valor == 1);
                    case "AX" -> accelX = valor;
                    case "AY" -> accelY = valor;
                }
            }
        } catch (NumberFormatException e) {
            // línea malformada, ignorar
        }
    }

    public void disconnect() {
        if (serialPort != null && serialPort.isOpened()) {
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    } 
}
