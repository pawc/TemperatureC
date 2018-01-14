package pl.pawc;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Communicator implements SerialPortEventListener{

    private Enumeration ports = null;

    private HashMap portMap = new HashMap();

    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    private InputStream input = null;
    private OutputStream output = null;

    private boolean bConnected = false;

    final static int TIMEOUT = 2000;

    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    String logText = "";
    String temperatureChar;
    String temperatureString = "";
    
    private TemperatureJdbcTemplate temperatureJdbcTemplate = null;
    
    public Communicator(TemperatureJdbcTemplate temperatureJdbcTemplate) {
    	this.temperatureJdbcTemplate = temperatureJdbcTemplate;
    }
    
	public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try {
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII){
                    temperatureChar = new String(new byte[] {singleData});
                    //System.out.print(temperatureChar);
                    temperatureString += temperatureChar;
                  
                }
                else{
                    //System.out.print("\n");
                    temperatureString += "\n";
                     
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Double tempC = Double.parseDouble(temperatureString);
                    temperatureString = "";
                    
                    Temperature temperature = new Temperature();
                    temperature.setTimestamp(timestamp);
                    temperature.setTempC(tempC);
                    
                    System.out.println(temperature.toString());
                    
                    temperatureJdbcTemplate.insert(temperature);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
		
	}
	
    public void searchForPorts(){
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL){
                portMap.put(curPort.getName(), curPort);
                System.out.println(curPort.getName());
            }
        }
    }
    
    public void connect(String input){
        String selectedPort = input;
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);

        CommPort commPort = null;

        try{
            commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
            serialPort = (SerialPort)commPort;
            bConnected = true;

            logText = selectedPort + " opened successfully.";
            System.out.println(logText);
            
        }
        catch (PortInUseException e){
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            System.out.println(logText);
        }
        catch (Exception e) {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            System.out.println(logText + "n");
        }
    }
    
    public boolean initIOStream() {
        boolean successful = false;

        try {
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            successful = true;
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            System.out.println(logText);
            return successful;
        }
    }
    
    public void initListener() {
        try{
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e){
            logText = "Too many listeners. (" + e.toString() + ")";
            System.out.println(logText);
        }
    }
    
    public void disconnect(){
        try{
            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            bConnected = false;

            logText = "Disconnected.";
            System.out.println(logText);
            
        }
        catch (Exception e){
            logText = "Failed to close " + serialPort.getName()
                              + "(" + e.toString() + ")";
            System.out.println(logText);
        }
    }
	
}