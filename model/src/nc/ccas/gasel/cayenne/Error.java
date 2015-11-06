package nc.ccas.gasel.cayenne;

public class Error extends RuntimeException {
	private static final long serialVersionUID = 3423761060339625791L;

	public Error(String message) {
		super(message);
	}

}
