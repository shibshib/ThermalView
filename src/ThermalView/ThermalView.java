package ThermalView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;


public class ThermalView implements Runnable, SerialPortEventListener {
	static CommPortIdentifier portId;
	static Enumeration portList;
	
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;
	
	public static void main(String[] args){
		portList = CommPortIdentifier.getPortIdentifiers();
		System.out.println(portList.hasMoreElements());
		while(portList.hasMoreElements()){
			portId = (CommPortIdentifier) portList.nextElement();
			if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL){
				if(portId.getName().equals("COM4")){
					ThermalView reader = new ThermalView();
				}
			}
		}
	}
	
	public ThermalView(){
		try{
			serialPort = (SerialPort) portId.open("ThermalViewApp", 2000);
		} catch (PortInUseException e) {
			System.out.println(e);
		}
		try{
			inputStream = serialPort.getInputStream();
		} catch (IOException e){
			System.out.println(e);
		}
		try{
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e){
			System.out.println(e);
		}
		
		serialPort.notifyOnDataAvailable(true);
		try{
			serialPort.setSerialPortParams(115200,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e){
			System.out.println(e);
		}
		
		readThread = new Thread(this);
		readThread.start();
	}
	
	@Override
	public void run() {
		try{
			Thread.sleep(20000);
		} catch (InterruptedException e){
			System.out.println(e);
		}
		
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch(event.getEventType()){
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[20];
			
			try{
				while(inputStream.available() > 0){
					int numBytes = inputStream.read(readBuffer);
				}
				System.out.print(new String(readBuffer));

			} catch (IOException e){
				System.out.println(e);
			}
			break;
		}
	}
	
}
