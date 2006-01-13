package ch.bbv.notification.server;

/**
 * RuntimeException for throwing on not implemented methods, RuntimeException because its undesired
 * to change method signature only for the NotSupportedException
 * @author Bruno Persi
 */
public class NotSupportedException extends RuntimeException {

	public NotSupportedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotSupportedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotSupportedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotSupportedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
