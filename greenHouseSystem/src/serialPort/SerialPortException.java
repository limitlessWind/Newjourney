package serialPort;

public class SerialPortException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8539008731081678079L;

	private String message;
	
	public SerialPortException() {
		super();
	}

	public SerialPortException(Throwable t) {
		super(t);
	}

	public SerialPortException(String message) {
		this.message = message;
	}

	public SerialPortException(String message, Throwable t) {
		super(t);
		this.message = message;
	}

	public SerialPortException(String message, String s) {
		super(s);
		this.message = message;
	}

	public SerialPortException(String message, String s, Throwable t) {
		super(s, t);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return super.toString() + " [message: " + message + "]";
	}
}
