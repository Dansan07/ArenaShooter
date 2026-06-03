package input;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * @author Usuario
 */
public class SerialManager {
    private SerialPort serialPort;
    private String buffer = "";

    public int joyX = 512;
    public int joyY = 512;
    public boolean joyButton = false;

    public int accelX = 0;
    public int accelY = 0;

    public void connect() {
        // Obtenemos los puertos disponibles usando jSerialComm
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            System.out.println("No se encontró puerto serial.");
            return;
        }

        // Seleccionamos el primer puerto disponible (ej: COM3)
        serialPort = ports[0]; 

        // Configurar parámetros del puerto (Reemplaza a setParams de JSSC)
        serialPort.setBaudRate(9600);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        serialPort.setParity(SerialPort.NO_PARITY);

        // Intentamos abrir el puerto
        if (serialPort.openPort()) {
            System.out.println("Conectado con éxito a: " + serialPort.getSystemPortName());
        } else {
            System.out.println("Error al abrir el puerto serial.");
            return;
        }

        // Configurar el Event Listener usando la estructura de jSerialComm
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                // Indicamos que queremos escuchar cuando haya datos disponibles para leer (RXCHAR)
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }
                
                // Leer los bytes entrantes
                byte[] newData = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(newData, newData.length);
                
                if (numRead > 0) {
                    // Convertimos los bytes a String y los acumulamos en el buffer
                    buffer += new String(newData, 0, numRead);
                    procesarBuffer();
                }
            }
        });
    }

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
            // Espera una trama como: JX:512,JY:510,JB:0
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
        if (serialPort != null && serialPort.isOpen()) {
            if (serialPort.closePort()) {
                System.out.println("Puerto serial cerrado correctamente.");
            } else {
                System.out.println("Error al intentar cerrar el puerto.");
            }
        }
    }
}