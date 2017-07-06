package serialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.TooManyListenersException;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SerialReader extends Observable implements Runnable, SerialPortEventListener{

	int delayRead = 200;
	boolean isOpen = false;
	SerialPort serialPort;
	InputStream inputStream;
	OutputStream outputStream;
	CommPortIdentifier port;
	
	public boolean isOpen() {
		return isOpen;
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		 try
	        {
	            // 等待1秒钟让串口把数据全部接收后在处理
	            Thread.sleep( delayRead );
	            System.out.println( "serialEvent[" + event.getEventType() + "]    " );
	        }
	        catch ( InterruptedException e )
	        {
	            e.printStackTrace();
	        }
	        switch ( event.getEventType() )
	        {
	            case SerialPortEvent.BI: // 10
	            	System.out.println("BI");
	            	break;
	            case SerialPortEvent.OE: // 7
	            	System.out.println("oe");
	            	break;
	            case SerialPortEvent.FE: // 9
	            	System.out.println("fe");
	            	break;
	            case SerialPortEvent.PE: // 8
	            	System.out.println("pe");
	            	break;
	            case SerialPortEvent.CD: // 6
	            	System.out.println("cd");
	            	break;
	            case SerialPortEvent.CTS: // 3
	            case SerialPortEvent.DSR: // 4
	            case SerialPortEvent.RI: // 5
	            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
	                break;
	            case SerialPortEvent.DATA_AVAILABLE: // 1
	                try
	                {
	                    // 多次读取,将所有数据读入
	                	int bufferLen = inputStream.available();
	                	byte[] readBuffer = null;
	                    while (bufferLen > 0) {	          
	                    	readBuffer = new byte[bufferLen];
	                    	 inputStream.read(readBuffer);
	                    	 bufferLen = inputStream.available();
	                     }                  
	                    setChanged();
	                    notifyObservers(readBuffer); 	                   
	                }
	                catch ( IOException e )
	                {
	                    e.printStackTrace();
	                }
	                break;
	        }
		
	}

	@Override
	public void run() {
		 try
	        {
	            Thread.sleep( 100 );
	        }  catch ( InterruptedException e ) {
	        }	
	}

	public void sendToPort(String order) throws SerialPortException{
		byte[] orderBytes = Utils.hexString2HexBytes(order);
//		byte[] orderBytes = order.getBytes();
		try {        	           
            outputStream.write(orderBytes);
            outputStream.flush();
            System.out.println("sended: " + order);
        } catch (IOException e) {
        	System.out.println("send order failed");
        	throw new SerialPortException("发送命令失败");
        	
        }
	}
	
	
	public void openPort(String portId, String rateId) throws SerialPortException {
		if (isOpen) {
			close();
		}
		try {
			port = CommPortIdentifier.getPortIdentifier(portId);
            serialPort = ( SerialPort ) port.open( "SerialReader", 2000 );
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            serialPort.addEventListener( this );
            serialPort.notifyOnDataAvailable( true );
            serialPort.setSerialPortParams( Integer.parseInt(rateId), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            isOpen = true;
		} catch ( PortInUseException e )
        {
            throw new SerialPortException( "端口已经被占用");
        }
        catch ( TooManyListenersException e )
        {
            throw new SerialPortException( "端口监听者过多");
        }
        catch ( UnsupportedCommOperationException e )
        {
            throw new SerialPortException( "端口操作命令不支持");
        }
        catch ( NoSuchPortException e )
        {
            throw new SerialPortException( "端口不存在");
        }
        catch ( IOException e )
        {
            throw new SerialPortException( "打开端口失败");
        }
		Thread readThread = new Thread( this );		//???question  thread
        readThread.start();
	}

	public void close() throws SerialPortException {
		 if (isOpen)
	        {
	            try
	            {
	            	serialPort.notifyOnDataAvailable(false);
	            	serialPort.removeEventListener();
	                inputStream.close();
	                outputStream.close();
	                serialPort.close();
	                isOpen = false;
	            } catch (IOException ex)
	            {
	            	throw new SerialPortException("关闭串口失败");
	            }
	        }
		
	}
	
	static List<String> findPort() {
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> portNames = new ArrayList<String>();
		while (portList.hasMoreElements()) {
			portNames.add(portList.nextElement().getName());
		}
		
		return portNames;
		
	}

}
