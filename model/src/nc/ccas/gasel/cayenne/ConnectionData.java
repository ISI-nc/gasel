package nc.ccas.gasel.cayenne;

public class ConnectionData {

	String userId;

	String passwd;

	public ConnectionData(String userId, String passwd) {
		this.userId = userId;
		this.passwd = passwd;
	}

	public String getUserId() {
		return userId;
	}

}
