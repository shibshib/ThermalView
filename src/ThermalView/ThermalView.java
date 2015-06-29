package ThermalView;

/*
* Takes input from USB interface and displays it as overlay on top of webcam view, creating a "heat map"
*/

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
	String tempHold = "";
	
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;
	
	List<String> fullGrid = new ArrayList<String>();
	
	ThermalWindow tw;
	
	boolean incompleteValue = false;
	String corruptValue = "";
	
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
		tw = new ThermalWindow();
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
			Thread.sleep(3);
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
			byte[] readBuffer = new byte[500];
			
			try{
				while(inputStream.available() > 0){
					int numBytes = inputStream.read(readBuffer);
				}
				String bufferString = new String(readBuffer);
				
				
				tokenizeBuffer(new String(readBuffer));

			//	System.out.println(new String(readBuffer));
				
			} catch (IOException e){
				System.out.println(e);
			}
			break;
		}
	}

	private synchronized void tokenizeBuffer(String readBuffer) {
		String[] splitBuffer = readBuffer.split("-");
		
		// There is a an incomplete value in our buffer, it's usually at the end, 
		// we add it to the next value coming in 
		// then clear the buffer
		if(incompleteValue){
			corruptValue += splitBuffer[0];
			fullGrid.add(corruptValue);
			splitBuffer[0] = "";
			incompleteValue = false;
			corruptValue = "";
		}
		for(String s : splitBuffer){
			// Now we must inspect each line and make sure it's complete, if not then we must "stitch" it with the next one
			s = s.trim();
		//	System.out.println(s.trim());
			if(s.length() < 5 && !s.equals("")){
		//		System.out.println("Incomplete value");
				corruptValue = s;
				incompleteValue = true;
			} else if(!s.equals("")) {
				fullGrid.add(s);
			//	System.out.println(fullGrid.size());
			}
		//	System.out.println(subSplit.length);
		}
		
		if(fullGrid.size() == 64){
			int localCount = 0;
			for(String s : fullGrid){
				System.out.print(s + "\t");
				if(localCount == 7){
					System.out.println("");
					localCount = 0;
				} else {
					localCount++;
				}
			}
			updateGridWindow(fullGrid);
			fullGrid.clear();
			System.out.println("----------------------");
		}
		
	}

	private void updateGridWindow(List<String> grid) {
		for(int i = 0; i < 64; i++){
			float val = Float.parseFloat(grid.get(i));
			if(val <= 25.00f){
				tw.components.get(63-i).setBackground(Color.BLUE);
			} else if(val >= 26.00f && val <= 29.00f){
				tw.components.get(63-i).setBackground(Color.ORANGE);
			} else if(val >= 30.00f){
				tw.components.get(63-i).setBackground(Color.RED);
			}
		}
	}
	
}
